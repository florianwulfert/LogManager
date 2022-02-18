import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {UserService} from "../../modules/user/service/user.service";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit{

  @Output() public expansionToggle = new EventEmitter();

  constructor(private userService: UserService) {}

  dataSource: any;

  listIsEmptyMessage: string = 'There are no users to show!';

  ngOnInit(): void {
    this.userService.getUsers().subscribe(result => {
      this.dataSource = result.body
    });
  }

  displayedColumns: string[] = ['name', 'birthdate', 'weight', 'height', 'favouriteColor', 'bmi', 'delete'];

  public onToggleExpansion = () => {
    this.expansionToggle.emit();
  }
}
