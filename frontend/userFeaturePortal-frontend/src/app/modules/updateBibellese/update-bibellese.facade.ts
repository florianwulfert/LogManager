import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {UpdateBibelleseState} from "./update-bibellese.state";
import {updateBibelleseAction,} from "./update-bibellese.actions";
import {updateBibellese} from "./update-bibellese.selector";
import {UpdateBibelleseRequest} from "./update-bibellese-request";

@Injectable({providedIn: 'root'})
export class UpdateBibelleseFacade {
  stateGetBibelleseResponse$ = this.updateBibelleseState.select(updateBibellese)

  constructor(
    private readonly updateBibelleseState: Store<UpdateBibelleseState>
  ) {
  }

  updateBibellese(request: UpdateBibelleseRequest): void {
    this.updateBibelleseState.dispatch(updateBibelleseAction(request));
  }
}
