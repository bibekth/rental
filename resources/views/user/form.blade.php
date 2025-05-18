@extends('layouts.app')
@section('content')
<div class="sa-contact-form-text">
    <h3 class="my-4">Create User</h3>
    @if ($errors->any())
    <ul>
        @foreach ($errors->all() as $error )
        <li style="color: red"> {{$error}} </li>
        @endforeach  
    </ul>
    @endif

    <form action="{{ route('user.store' )}}" method="POST" enctype="multipart/form-data">
        @csrf
        <div class="sa-text-field-block">
            <label for="">User Name</label>
            <input type="text" name="name" id="nameid" placeholder="Enter User Name">
        </div>
        <div class="sa-text-field-block">
            <label for="">Email</label>
            <input type="email" name="email" id="emailid" placeholder="Enter Email Address">
        </div>
        <div class="sa-text-field-block">
            <label for="phoneid">Phone Number</label>
            <input type="text" name="phonenumber" id="phoneid" placeholder="Enter user phone number">
        </div>
        <div class="sa-text-field-block">
            <label for="">Password</label>
            <input type="password" name="password" id="passwordid" placeholder="Enter Password">
        </div>
        <div class="sa-text-field-block">
            <label for="password_confirmation">Confirm Password</label>
            <input type="password" name="password_confirmation" id="password_confirmation" placeholder="Confirm password">
        </div>
        <div class="sa-text-field-block">
            <label for="roleid">Role</label>
            <select name="role" id="roleid">
                <option value="" selected disabled>Select Role</option>
                <option value="admin">Admin</option>
                <option value="user">User</option>
            </select>
        </div>
        {{-- <div class="sa-text-field-block">
            <label for="">Role</label>
            <select name="role" id="roleid">
                <option value="" selected disabled>Select Role</option>
                <option value="admin">Admin</option>
                <option value="user">User</option>
            </select>
        </div> --}}
        <div class="sa-form-button">
            <input type="submit" value="Submit">
        </div>
    </form>
</div>
@endsection
