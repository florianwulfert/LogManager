import {Component, Inject, OnDestroy, OnInit} from "@angular/core";
import {UpdateBibelleseFacade} from "../../../modules/updateBibellese/update-bibellese.facade";
import {UpdateBibelleseRequest} from "../../../modules/updateBibellese/update-bibellese-request";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {takeUntil} from "rxjs/operators";
import {ActorFacade} from "../../../modules/actor/actor.facade";
import {Subject} from "rxjs";
import {FeatureManager} from "../../../../assets/utils/feature.manager";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BibleComponent} from "../bible.component";

@Component({
  selector: 'app-bibellese-update',
  templateUrl: './bibellese-update.component.html',
  styleUrls: ['./bibellese-update.component.scss']
})
export class BibelleseUpdateComponent implements OnInit, OnDestroy {

  constructor(private updateBibelleseFacade: UpdateBibelleseFacade,
              private actorFacade: ActorFacade,
              public featureManager: FeatureManager,
              public dialogRef: MatDialogRef<BibleComponent>,
              @Inject(MAT_DIALOG_DATA) public data: UpdateBibelleseRequest) {
  }

  userAvailable: boolean = false
  onDestroy = new Subject()
  labelList: string[] = [];
  lieblingsverse: string[] = [];
  lieblingsversTexte: string[] = [];

  ngOnInit() {
    this.fillFormWithData()
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
    this.updateBibelleseFacade.stateGetBibelleseResponse$.pipe(takeUntil(this.onDestroy)).subscribe(r => {
      this.dialogRef.close(this.data)

    })

  }

  public form: FormGroup = new FormGroup({
    id: new FormControl(''),
    bibelabschnitt: new FormControl('', [Validators.required]),
    lieblingsverse: new FormControl('',),
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

  fillFormWithData() {
    this.form.controls['id'].setValue(this.data.id)
    this.form.controls['bibelabschnitt'].setValue(this.data.bibelabschnitt)

    for (let item of this.data.labels) {
      this.labelList.push(item)
    }
    for (let item of this.data.lieblingsverse) {
      this.lieblingsverse.push(item)
    }
    for (let item of this.data.versText) {
      this.lieblingsversTexte.push(item)
    }

    this.form.controls['kommentar'].setValue(this.data.kommentar)
    this.form.controls['leser'].setValue(this.data.leser)
  }
}
