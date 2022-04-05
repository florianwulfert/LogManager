import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {map} from "rxjs/internal/operators";
import {catchError} from "rxjs/operators";
import {CalculateBmiRequest} from "./calculate-bmi/dto/calculate-bmi-request";
import {CalculateBmiResponse} from "./calculate-bmi/dto/calc-bmi-response";
import {FeatureManager} from "../../../assets/utils/feature.manager";

const API_CALC_BMI = 'http://localhost:8081/bmi';

@Injectable({
  providedIn: 'root'
})
export class BmiService {
  constructor(private readonly http: HttpClient, private featureManager: FeatureManager) {
  }

  calcBmi(calcBmiRequest: CalculateBmiRequest): Observable<CalculateBmiResponse> {
    return this.http.post<CalculateBmiResponse>(API_CALC_BMI, {...calcBmiRequest}, {
      observe: 'response'
    }).pipe(
      map((r) => {
        this.featureManager.openSnackbar(r.body?.resultMessage);
        return r.body || {
          resultMessage: ""
        }
      }),
      catchError((err) => {
        if(err.error instanceof Object) {
          this.featureManager.openSnackbar(err.error.text);
        } else {
          this.featureManager.openSnackbar(err.error);
        }
        return throwError('Due to technical issues it is currently not possible to calculate the BMI.');
      })
    );
  }
}
