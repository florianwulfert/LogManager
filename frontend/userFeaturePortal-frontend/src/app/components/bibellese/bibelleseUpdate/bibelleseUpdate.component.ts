import {Component} from "@angular/core";
import {UpdateBibelleseFacade} from "../../../modules/updateBibellese/update-bibellese.facade";
import {UpdateBibelleseRequest} from "../../../modules/updateBibellese/update-bibellese-request";

@Component({
  selector: 'app-bibelleseUpdate',
  templateUrl: './bibelleseUpdate.component.html',
  styleUrls: ['./bibelleseUpdate.component.scss']
})
export class BibelleseUpdateComponent {

  constructor(private updateBibelleseFacade: UpdateBibelleseFacade) {
  }

  updateBibellese(id: string) {
    let request = new UpdateBibelleseRequest()
    this.updateBibelleseFacade.updateBibellese(request)
  }
}
