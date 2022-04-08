package project.logManager.service.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.common.dto.log.LogMessageDto;
import project.logManager.common.dto.log.LogRequestDto;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.model.repository.LogRepository;
import project.logManager.service.validation.LogValidationService;

/**
 * @author - EugenFriesen 12.02.2021
 */
@Transactional
@Service
@RequiredArgsConstructor
public class LogService {

  private static final Logger LOGGER = LogManager.getLogger(LogService.class);

  private final LogRepository logRepository;
  private final LogValidationService logValidationService;
  private final LogDTOMapper logDTOMapper;

  public List<LogDTO> getLogs(
      String severity, String message, LocalDateTime startDate, LocalDateTime endDate) {

    return logDTOMapper.logsToLogDTOs(
        logRepository.findLogs(severity, message, startDate, endDate));
  }

  public String addLog(LogRequestDto logRequestDto) {
    logValidationService.checkIfAnyEntriesAreNull(logRequestDto);
    logValidationService.validateSeverity(logRequestDto.getSeverity());
    LogMessageDto logMessage = logValidationService.validateMessage(logRequestDto.message);
    User user = logValidationService.checkActor(logRequestDto.user);
    saveLog(logMessage.getMessage(), logRequestDto.getSeverity(), user);

    logMessage.setReturnMessage(
        logMessage.getReturnMessage()
            + String.format(
            InfoMessages.MESSAGE_SAVED, logMessage.getMessage(), logRequestDto.getSeverity()));
    LOGGER.info(
        String.format(
            InfoMessages.MESSAGE_SAVED, logMessage.getMessage(), logRequestDto.getSeverity()));
    return logMessage.getReturnMessage();
  }

  private void saveLog(String message, String severity, User user) {
    Log log = new Log();
    log.setMessage(message);
    log.setSeverity(severity);
    log.setUser(user);
    Date timestamp = new Date();
    log.setTimestamp(timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    logRepository.save(log);
  }

  public Log searchLogsByID(Integer id) {
    return logRepository.findById(id).isPresent() ? logRepository.findById(id).get() : null;
  }

  public String deleteById(Integer id) {
    logRepository.deleteById(id);
    LOGGER.info(String.format(InfoMessages.ENTRY_DELETED_ID, id));
    return String.format(InfoMessages.ENTRY_DELETED_ID, id);
  }

  public boolean existLogByUserToDelete(User actor) {
    List<Log> logs = logRepository.findByUser(actor);
    LOGGER.info(String.format(InfoMessages.LOGS_BY_USER, actor));
    return !logs.isEmpty();
  }

  public String deleteBySeverity(String severity) {
    List<Log> deletedLogs = logRepository.deleteBySeverity(severity);
    if (deletedLogs.isEmpty()) {
      LOGGER.info(ErrorMessages.NO_ENTRIES_FOUND);
      return ErrorMessages.NO_ENTRIES_FOUND;
    }

    StringBuilder sb = new StringBuilder();
    StringBuilder iDs = new StringBuilder();

    for (Log log : deletedLogs) {
      iDs.append(log.getId());
      if (deletedLogs.lastIndexOf(log) < deletedLogs.size() - 1) {
        iDs.append(", ");
      }
    }

    sb.append("Entries with the ID(s) ").append(iDs).append(" were deleted from database.");
    return sb.toString();
  }

  public String deleteAll() {
    logRepository.deleteAll();
    LOGGER.info(InfoMessages.ALL_LOGS_DELETED);
    return InfoMessages.ALL_LOGS_DELETED;
  }
}
