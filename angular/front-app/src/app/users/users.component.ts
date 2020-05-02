import { Component, OnInit } from '@angular/core';
import { User } from "../DTO/User";
import { UserService } from './users.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
    public users: User[];
    constructor(private userService: UserService) { }

    ngOnInit() {
      let token = localStorage.getItem('token');
      this.userService.getUsers(token).subscribe(
        users => this.users = users
      )
    }
}
