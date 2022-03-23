import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {map} from "rxjs/internal/operators";
import {catchError} from "rxjs/operators";
import {CalculateBmiRequest} from "./calculate-bmi/dto/calculate-bmi-request";
import {CalculateBmiResponse} from "./calculate-bmi/dto/calc-bmi-response";

const API_CALC_BMI = 'http://localhost:8081/bmi';

@Injectable({
  providedIn: 'root'
})
export class BmiService {
  constructor(private readonly http: HttpClient) {
  }


  calcBmi(calcBmiRequest: CalculateBmiRequest): Observable<CalculateBmiResponse> {
    return this.http.post<CalculateBmiResponse>(API_CALC_BMI, {...calcBmiRequest}, {
      observe: 'response'
    }).pipe(
      map((r) => {
        return r.body || {
          result: r.body ? r.body : ''
        }
      }),
      catchError(() => {
        return throwError('Due to technical issues it is currently not possible to calculate the BMI.');
      })
    );
  }
}
