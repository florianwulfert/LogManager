import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActorFacade} from "../../modules/actor/actor.facade";
import {BibelleseFacade} from "../../modules/bibellese/bibellese.facade";
import {GetBibelleseRequest} from "../../modules/bibellese/getBibellese/get-bibellese-request";
import {AddBibelleseRequest} from "../../modules/bibellese/addBibellese/add-bibellese-request";
import {DeleteBibelleseRequest} from "../../modules/bibellese/deleteBibellese/delete-bibellese-request";
import {takeUntil} from "rxjs/operators";
import {Subject} from "rxjs";
import {FeatureManager} from "../../../assets/utils/feature.manager";
import {MatDialog} from "@angular/material/dialog";
import {UsersFacade} from "../../modules/users/users.facade";
import {BibelleseUpdateComponent} from "./bibelleseUpdate/bibellese-update.component";
import {UpdateBibelleseRequest} from "../../modules/updateBibellese/update-bibellese-request";

@Component({
  selector: 'app-bible',
  templateUrl: './bible.component.html',
  styleUrls: ['./bible.component.scss']
})

export class BibleComponent implements OnInit, OnDestroy {

  constructor(private bibelleseFacade: BibelleseFacade,
              private _snackBar: MatSnackBar,
              private actorFacade: ActorFacade,
              public featureManager: FeatureManager,
              public userFacade: UsersFacade,
              public dialog: MatDialog) {
  }

  displayedColumns: string[] = ['bibelabschnitt', 'lieblingsverse', 'versText', 'labels', 'leser', 'kommentar', 'update', 'delete'];
  labelList: string[] = [];
  lieblingsverse: string[] = [];
  lieblingsversTexte: string[] = [];
  userAvailable: boolean = false
  onDestroy = new Subject()
  isExpanded = false;
  filterButtonPressed: boolean = false
  bibelabschnitte: any
  leserList: any
  name: string | undefined
  labels: any
  bibellese: any

  dataSource: any;


  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

  ngOnInit() {
    this.getUserList()
    this.getBibellese()
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

  openDialog(element: UpdateBibelleseRequest): void {
    const dialogRef = this.dialog.open(BibelleseUpdateComponent, {
      width: '1000px',
      data: {
        id : element.id,
        bibelabschnitt : element.bibelabschnitt,
        lieblingsverse : element.lieblingsverse,
        versText : element.versText,
        labels : element.labels,
        kommentar : element.kommentar,
        leser : element.leser
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.dataSource = new MatTableDataSource(result)
      this.getBibellese()
    });
  }

  public form: FormGroup = new FormGroup({
    bibelabschnitt: new FormControl('', [Validators.required]),
    lieblingsvers: new FormControl('',),
    versText: new FormControl('',),
    labels: new FormControl(''),
    kommentar: new FormControl('', [Validators.required]),
    leser: new FormControl('', [Validators.required]),
  })

  public prepareAddBibelleseRequest(request: AddBibelleseRequest) {
    request.bibelabschnitt = this.form.get("bibelabschnitt")?.value
    request.lieblingsverse = this.lieblingsverse
    request.versText = this.lieblingsversTexte
    request.labels = this.labelList
    request.kommentar = this.form.get("kommentar")?.value
    request.leser = this.form.get("leser")?.value
    return request;
  }

  addBibellese(): void {
    let request = new AddBibelleseRequest()
    this.prepareAddBibelleseRequest(request)
    this.bibelleseFacade.addBibellese(request);
    this.isExpanded = false;
  }

  getBibellese(): void {
    let request = new GetBibelleseRequest();
    this.bibelleseFacade.getBibellese(request)
    this.bibelleseFacade.stateGetBibelleseResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      this.dataSource = new MatTableDataSource(result)
      this.dataSource.paginator = this.paginator;
      this.bibellese = result
    })
  }

  deleteBibellese(element: any): void {
    let request = new DeleteBibelleseRequest()
    request.id = element.id
    this.bibelleseFacade.deleteBibellese(request)
  }

  resetForm() {
    this.form.reset();
    this.labelList = [];
    this.lieblingsverse = [];
    this.lieblingsversTexte = [];
  }

  filterBibellese() {
    this.getBibellese()
    this.filterButtonPressed = true
  }

  public formFilter: FormGroup = new FormGroup({
    bibelabschnitt: new FormControl(''),
    lieblingsvers: new FormControl(''),
    versText: new FormControl(''),
    label: new FormControl(''),
    kommentar: new FormControl(''),
    leser: new FormControl('')
  })

  getUserList(): void {
    this.userFacade.getUsers();
    this.userFacade.stateGetUsersResponse$.pipe(takeUntil(this.onDestroy)).subscribe(result => {
      this.leserList = result
    });
  }
}
