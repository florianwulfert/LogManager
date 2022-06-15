import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {GetListsForBibelleseFilterState} from "./getListsForBibelleseFilter.state";
import {getAllBibelleseAction,} from "./getListsForBibelleseFilter.actions";
import {getBibellese} from "./getListsForBibelleseFilter.selector";

@Injectable({providedIn: 'root'})
export class GetListsForBibelleseFilterFacade {
  stateGetListsForBibelleseFilterResponse$ = this.getListsForBibelleseFilterState.select(getBibellese)

  constructor(
    private readonly getListsForBibelleseFilterState: Store<GetListsForBibelleseFilterState>
  ) {
  }

  getAllBibellese(): void {
    this.getListsForBibelleseFilterState.dispatch(getAllBibelleseAction())
  }
}
