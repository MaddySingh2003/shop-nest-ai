import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { ProfileService } from "../../services/profile.service";
import { FormsModule } from "@angular/forms";

@Component({
    selector:'app-profile',
    standalone:true,
    imports:[CommonModule,FormsModule],
    templateUrl:'./profile.html'
})
export class ProfileComponent implements OnInit{

    user:any=null;
    loading=true;
    profile:any={};
    edit:boolean=false;

    constructor(
        private profileService:ProfileService,
        private cdr:ChangeDetectorRef
    ){}

    ngOnInit() {
        this.loadProfile();
    }

    loadProfile(){
        this.profileService.getProfile().subscribe(res=>{
            console.log("PROFILE",res);
            this.user=res;
            this.loading=false;
            this.cdr.detectChanges();
        })
    }
   updateProfile(){
    this.profileService.updateProfile(this.profile).subscribe(res=>{
      this.profile=res;
      this.edit=false;
      alert("Profile updated");
      this.cdr.detectChanges();
    });
  }

}