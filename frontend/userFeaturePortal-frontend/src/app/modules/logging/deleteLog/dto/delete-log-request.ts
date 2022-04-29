import {UserDto} from "../../../user/getUser/user.dto";

export class DeleteLogRequest {
  id: string | undefined
  severity: string | undefined
  message: string | undefined
  startDateTime: string | undefined
  endDateTime: string | undefined
  user: UserDto | undefined
}
