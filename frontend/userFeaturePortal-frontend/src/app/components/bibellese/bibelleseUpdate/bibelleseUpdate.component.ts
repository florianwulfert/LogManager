import {Component, OnDestroy, OnInit} from "@angular/core";
import {UpdateBibelleseFacade} from "../../../modules/updateBibellese/update-bibellese.facade";
import {UpdateBibelleseRequest} from "../../../modules/updateBibellese/update-bibellese-request";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {takeUntil} from "rxjs/operators";
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {Subject} from "rxjs";
import {FeatureManager} from "../../../../assets/utils/feature.manager";

@Component({
  selector: 'app-bibelleseUpdate',
  templateUrl: './bibelleseUpdate.component.html',
  styleUrls: ['./bibelleseUpdate.component.scss']
})
export class BibelleseUpdateComponent implements OnInit, OnDestroy {

  constructor(private updateBibelleseFacade: UpdateBibelleseFacade,
              private actorFacade: ActorFacade,
              public featureManager: FeatureManager) {
  }

  isExpanded = false;
  userAvailable: boolean = false
  onDestroy = new Subject()
  labelList: string[] = [];
  lieblingsverse: string[] = [];
  lieblingsversTexte: string[] = [];

  ngOnInit() {
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

  updateBibellese() {
    let request = new UpdateBibelleseRequest()
    this.updateBibelleseFacade.updateBibellese(request)
    this.isExpanded = false;
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
}
