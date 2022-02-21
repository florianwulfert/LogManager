import {HttpClient, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {GetUserResponse} from 'src/app/modules/user/dto/get-user-response';

const API_BASE = 'http://localhost:8081/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private readonly http: HttpClient) {}

  getUsers(): Observable<HttpResponse<GetUserResponse>> {
    return this.http.get<GetUserResponse>(API_BASE, {
      observe: 'response'
    })
  }
}
