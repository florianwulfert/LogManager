import {Component, OnDestroy, OnInit} from "@angular/core";
import {UpdateBibelleseFacade} from "../../../modules/updateBibellese/update-bibellese.facade";
import {UpdateBibelleseRequest} from "../../../modules/updateBibellese/update-bibellese-request";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {takeUntil} from "rxjs/operators";
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {Subject} from "rxjs";
import {FeatureManager} from "../../../../assets/utils/feature.manager";
import {GetBibelleseRequest} from "../../../modules/bibellese/getBibellese/get-bibellese-request";
import {BibelleseFacade} from "../../../modules/bibellese/bibellese.facade";

@Component({
  selector: 'app-bibelleseUpdate',
  templateUrl: './bibelleseUpdate.component.html',
  styleUrls: ['./bibelleseUpdate.component.scss']
})
export class BibelleseUpdateComponent implements OnInit, OnDestroy {

  constructor(private updateBibelleseFacade: UpdateBibelleseFacade,
              private actorFacade: ActorFacade,
              public featureManager: FeatureManager,
              private bibelleseFacade: BibelleseFacade) {
  }

  isExpanded = false;
  userAvailable: boolean = false
  onDestroy = new Subject()
  labelList: string[] = [];
  lieblingsverse: string[] = [];
  lieblingsversTexte: string[] = [];

  ngOnInit() {
    this.getBibelleseData()
    this.actorFacade.stateActorIsValid$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      if (r) {
        this.userAvailable = true
      }
    })
  }

  ngOnDestroy() {
    this.onDestroy.next(null)
    this.onDestroy.complete()
  }

  prepareUpdateBibelleseRequest(updateBibelleseRequest: UpdateBibelleseRequest) {
    updateBibelleseRequest.id = this.form.get("id")?.value
    updateBibelleseRequest.bibelabschnitt = this.form.get("bibelabschnitt")?.value
    updateBibelleseRequest.lieblingsverse = this.lieblingsverse
    updateBibelleseRequest.versText = this.lieblingsversTexte
    updateBibelleseRequest.labels = this.labelList
    updateBibelleseRequest.kommentar = this.form.get("kommentar")?.value
    updateBibelleseRequest.leser = this.form.get("leser")?.value
    return updateBibelleseRequest;
  }

  updateBibellese() {
    let request = new UpdateBibelleseRequest()
    request = this.prepareUpdateBibelleseRequest(request)
    this.updateBibelleseFacade.updateBibellese(request)

  }

  public form: FormGroup = new FormGroup({
    bibelabschnitt: new FormControl('', [Validators.required]),
    lieblingsvers: new FormControl('',),
    versText: new FormControl('',),
    labels: new FormControl(''),
    kommentar: new FormControl('', [Validators.required]),
    leser: new FormControl('', [Validators.required]),
  })

  resetForm() {
    this.form.reset();
    this.labelList = [];
    this.lieblingsverse = [];
    this.lieblingsversTexte = [];
  }

  getBibelleseData() {
    let request = new GetBibelleseRequest()
    this.bibelleseFacade.getBibellese(request)
    this.bibelleseFacade.stateGetBibelleseResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      console.log(result)
    })
  }
}
