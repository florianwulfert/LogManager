import {Component} from '@angular/core';
import {UserService} from "../../modules/user/service/user.service";

export interface UserAtributes {
  id: number;
  name: string;
  birthdate: string;
  weight: number;
  height: number;
  favouriteColor: string;
  bmi: number;
}

const ELEMENT_DATA: UserAtributes[] = [
  {id: 1, name: 'Peter', birthdate: new Date(2001,12,19).toDateString(), weight: 75, height: 1.80, favouriteColor: "blue", bmi: 23.14},
  {id: 2, name: 'Hans', birthdate: new Date(2002,2,8).toDateString(), weight: 100, height: 1.80, favouriteColor: "yellow", bmi: 30.86},
  {id: 3, name: 'Werner', birthdate: new Date(1992,3,24).toDateString(), weight: 80, height: 1.80, favouriteColor: "red", bmi: 24.69},
];

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent {

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getUsers().subscribe(result => {
      console.log(result);
    });
  }

  displayedColumns: string[] = ['id', 'name', 'birthdate', 'weight', 'height', 'favouriteColor', 'bmi'];
  dataSource = ELEMENT_DATA;
}
