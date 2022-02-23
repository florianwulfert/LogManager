import {Observable, Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";

export class SubscriptionManager {
  public readonly observableCollection = new Subject();

  constructor() {}

  add(subject: Observable<any>): Observable<any> {
    return subject.pipe(takeUntil(this.observableCollection));
  }

  clear(): void {
    this.observableCollection.next();
    this.observableCollection.complete();
  }
}
