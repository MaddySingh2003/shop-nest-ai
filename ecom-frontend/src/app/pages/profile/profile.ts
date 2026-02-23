import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ProfileService } from "../../services/profile.service";
import { FormsModule } from "@angular/forms";
import { AddressService } from "../../services/adress.service";

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html'
})
export class ProfileComponent implements OnInit {

  user: any = null;
  loading = true;
  profile: any = {};
  edit: boolean = false;

  addresses: any[] = [];
  selectedAddressId: number | null = null;

  showAddressForm = false;

  newAddress: any = {
    fullName: '',
    phone: '',
    street: '',
    city: '',
    state: '',
    zipCode: '',
    country: 'India'
  };

  constructor(
    private profileService: ProfileService,
    private addressService: AddressService
  ) {}

  ngOnInit() {
    this.loadProfile();
    this.loadAddresses();
  }

  loadProfile() {
    this.profileService.getProfile().subscribe(res => {
      this.user = res;
      this.profile = { ...res };
      this.loading = false;
    });
  }

  updateProfile() {
    this.profileService.updateProfile(this.profile).subscribe(res => {
      this.user = res;
      this.profile = { ...res };
      this.edit = false;
      alert("Profile updated");
    });
  }

  loadAddresses() {
    this.addressService.getMyAddresses().subscribe((res: any) => {
      this.addresses = res || [];
      if (this.addresses.length > 0) {
        this.selectedAddressId = this.addresses[0].id;
      }
    });
  }

  saveAddress() {
    this.addressService.addAddress(this.newAddress).subscribe(() => {
      this.showAddressForm = false;

      // ✅ reset properly
      this.newAddress = {
        fullName: '',
        phone: '',
        street: '',
        city: '',
        state: '',
        zipCode: '',
        country: 'India'
      };

      this.loadAddresses();
    });
  }

  editAddress(a: any) {
    this.newAddress = { ...a };
    this.showAddressForm = true;
  }

  deleteAddress(id: number) {
    if (!confirm("Delete this address?")) return;

    this.addressService.deleteAddress(id).subscribe(() => {
      this.loadAddresses();
    });
  }
}