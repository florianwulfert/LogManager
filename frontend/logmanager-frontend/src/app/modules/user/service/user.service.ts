import {HttpClient, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UserDto} from 'src/app/modules/user/dto/userDto';

const API_BASE = 'http://localhost:8081/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private readonly http: HttpClient) {}

  getUsers(): Observable<HttpResponse<UserDto>> {
    return this.http.get<UserDto>(API_BASE, {
      observe: 'response'
    })
  }
}
