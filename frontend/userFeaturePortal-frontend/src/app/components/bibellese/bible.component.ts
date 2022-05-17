import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {MatTableDataSource} from "@angular/material/table";
import {SubscriptionManager} from "../../../assets/utils/subscription.manager";
import {MatPaginator} from "@angular/material/paginator";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActorFacade} from "../../modules/actor/actor.facade";
import {BibelleseFacade} from "../../modules/bibellese/bibellese.facade";
import {GetBibelleseRequest} from "../../modules/bibellese/getBibellese/get-bibellese-request";
import {AddBibelleseRequest} from "../../modules/bibellese/addBibellese/add-bibellese-request";
import {DeleteBibelleseRequest} from "../../modules/bibellese/deleteBibellese/delete-bibellese-request";

@Component({
  selector: 'app-bible',
  templateUrl: './bible.component.html',
  styleUrls: ['./bible.component.scss']
})

export class BibleComponent implements OnInit, OnDestroy {

  constructor(private bibelleseFacade: BibelleseFacade, private _snackBar: MatSnackBar, private actorFacade: ActorFacade) {
  }

  subscriptionManager = new SubscriptionManager();
  displayedColumns: string[] = ['text', 'lieblingsvers', 'lieblingsversText', 'label', 'leser', 'kommentar', 'delete'];
  bibellese: any
  userAvailable: boolean = false

  dataSource: any;

  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit() {
    this.getBibellese()
    this.subscriptionManager.add(this.actorFacade.stateActorIsValid$).subscribe(r => {
      if (r === true && r !== undefined) {
        this.userAvailable = true
      }
    })
  }

  ngOnDestroy() {
    this.subscriptionManager.clear()
  }

  getBibellese(): void {
    let request = new GetBibelleseRequest();
    this.bibelleseFacade.getBibellese(request)
    this.subscriptionManager.add(this.bibelleseFacade.stateGetBibelleseResponse$).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator;
    })
  }

  public form: FormGroup = new FormGroup({
    lieblingsvers: new FormControl('', [Validators.required]),
    text: new FormControl('', [Validators.required]),
  })

  prepareAddBookRequest(request: AddBibelleseRequest) {
    request.lieblingsverse = this.form.get("lieblingsvers")?.value
    request.bibelabschnitt = this.form.get("text")?.value
    return request;
  }

  addBibellese(): void {
    let request = new AddBibelleseRequest()
    this.prepareAddBookRequest(request)
    this.bibelleseFacade.addBibellese(request);
  }

  deleteBibellese(element: any): void {
    let request = new DeleteBibelleseRequest()
    let elementValues = Object.keys(element).map(key => element[key])
    request.id = elementValues[0]
    this.bibelleseFacade.deleteBibellese(request)
  }
}
