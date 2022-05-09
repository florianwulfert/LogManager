import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {BibelleseState} from "./bibellese.state";
import {
  addBibelleseAction,
  deleteBibelleseAction,
  getBibelleseAction,
} from "./bibellese.actions";
import {getBibellese} from "./bibellese.selector";
import {DeleteBibelleseRequest} from "./deleteBibellese/delete-bibellese-request";
import {AddBibelleseRequest} from "./addBibellese/add-bibellese-request";
import {GetBibelleseRequest} from "./getBibellese/get-bibellese-request";

@Injectable({providedIn: 'root'})
export class BibelleseFacade {
  stateGetBibelleseResponse$ = this.bibelleseState.select(getBibellese)

  constructor(
    private readonly bibelleseState: Store<BibelleseState>
  ) {
  }

  getBibellese(request: GetBibelleseRequest): void {
    this.bibelleseState.dispatch(getBibelleseAction(request))
  }

  addBibellese(request: AddBibelleseRequest): void {
    this.bibelleseState.dispatch(addBibelleseAction(request));
  }

  deleteBibellese(request: DeleteBibelleseRequest): void {
    this.bibelleseState.dispatch(deleteBibelleseAction(request))
  }
}
