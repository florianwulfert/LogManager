import {GetLogsRequest} from "../../getLogs/dto/getLogs-request";
import {AddLogRequest} from "./add-log-request";

export class AddLogRequestWithFilter {
  addLogRequest: AddLogRequest = new AddLogRequest()
  getLogsRequest: GetLogsRequest = new GetLogsRequest()
  user: string | undefined
}
