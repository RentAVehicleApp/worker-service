package rent.vehicle.workerserviceapp.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.worker.ResponseWorkerDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    private static final int HEAD_LIMIT = 20;

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Pointcut("execution(public * rent.vehicle.workerserviceapp.controller.worker.*.*(..))")
    public void controllerLog() {

    }

    @Pointcut("execution(public * rent.vehicle.workerserviceapp.service.worker.WorkerClientServiceImpl.*(..))")
    public void serviceLog() {

    }

    @Before("controllerLog()")
    public void doBeforeController(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        if (request != null) {
            log.info("NEW REQUEST: IP: {}, URL: {}, HTTP_METHOD: {}, CONTROLLER_METHOD: {},{}",
                    request.getRemoteAddr(),
                    request.getRequestURL(),
                    request.getMethod(),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }
    }

    @Before("serviceLog()")
    public void doBeforeService(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        Object[] args = joinPoint.getArgs();
        String stringArgs = args.length > 0 ? Arrays.toString(args) : "METHOD HAS NO ARGUMENT";

        log.info("RUN SERVICE: SERVICE_METHOD: {},{}\nMETHOD_ARGS: [{}]",
                className,
                methodName,
                stringArgs);
    }

    @AfterReturning(returning = "returnObject", pointcut = "controllerLog()")
    public void doAfterReturning(Object returnObject){

        try {
            if (returnObject == null) {
                log.info("RETURN OBJECT: NULL");
                return;
            }

            if (returnObject instanceof ResponseWorkerDto||returnObject instanceof ResponseTicketDto) {
                {
                    log.info("RETURN OBJECT: {}", MAPPER.writeValueAsString(saveView(returnObject)));
                }

            }
            if (returnObject instanceof CustomPage<?> col) {
                var head = col.getContent().stream().limit(HEAD_LIMIT).map(this::saveView).toList();
                log.info("← returned list size={}, head={}",col.getContent().size(),
                        MAPPER.writeValueAsString(head));
                return;
            }
        }catch (Exception e) {
            // На случай проблем сериализации (ленивые связи и т.п.)
            log.info("← returned (fallback) {}", String.valueOf(returnObject));
        }

    }

    private Object saveView(Object returnObject) {
        if (returnObject == null) return null;
        if (returnObject instanceof ResponseWorkerDto) {
            var tickets = ((ResponseWorkerDto) returnObject).getAssignedTickets() != null ? 
                    ((ResponseWorkerDto) returnObject).getAssignedTickets() : 
                    java.util.List.<ResponseTicketDto>of();
            var ticketHead = tickets.stream()
                    .limit(HEAD_LIMIT)
                    .map(this::ticketHead)  // см. метод ниже
                    .toList();
           Map<String,Object> map = new LinkedHashMap<>();
           map.put("login", ((ResponseWorkerDto) returnObject).getLogin());
            map.put("name", ((ResponseWorkerDto) returnObject).getName());
            map.put (   "ticketsCount", tickets.size());
            map.put(     "tickets", ticketHead);
          return map;
        }
        if (returnObject instanceof ResponseTicketDto) {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("id", ((ResponseTicketDto) returnObject).getId());
            m.put("header", ((ResponseTicketDto) returnObject).getHeader());
            // ограничим длину problem, чтобы не раздувать логи
            m.put("problem", trim(((ResponseTicketDto) returnObject).getProblem(),200));
            m.put("status", ((ResponseTicketDto) returnObject).getTicketStatus());
            m.put("assignedTo", ((ResponseTicketDto) returnObject).getAssignedTo() != null
                    ? ((ResponseTicketDto) returnObject).getAssignedTo().getLogin()
                    : null);
            return m;
        }
        return returnObject;
    }

    private Map<String,Object> ticketHead(ResponseTicketDto t) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("id", t.getId());
        m.put("header", t.getHeader());
        // ограничим длину problem, чтобы не раздувать логи
        m.put("problem", trim(t.getProblem(),200));
        m.put("status", t.getTicketStatus());
        m.put("assignedTo", t.getAssignedTo() != null ? t.getAssignedTo().getLogin() : null);
        return m;
    }
    private static String trim(String s, int max) {
        if (s == null) return null;                      // <-- защита от null
        if (max < 0) throw new IllegalArgumentException("max >= 0");
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }
    @After("controllerLog()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("CONTROLLER METHOD EXECUTED SUCCESSFULLY {},{} ",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }
    @Around("controllerLog()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime  = System.currentTimeMillis() - start;

        log.info("EXECUTION METHOD: {},{}\n EXECUTION TIME: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                executionTime);
    return proceed;
    }
    @AfterThrowing(throwing = "ex",pointcut = "controllerLog()")
    public void throwsException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.error("EXCEPTION IN: {},{} WITH ARGUMENTS:{},EXCEPTION MESSAGE: {}",
               className,
                methodName,
                Arrays.toString(joinPoint.getArgs()),
                ex.getMessage());
    }

}


