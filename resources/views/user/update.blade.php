@extends('layouts.app')
@section('content')
<div class="sa-contact-form-text">
    <h3 class="my-4">Update User</h3>
    @if ($errors->any())
    <ul>
        @foreach ($errors->all() as $error )
        <li style="color: red"> {{$error}} </li>
        @endforeach  
    </ul>
    @endif

    <form action="{{ route('user.update', $user->id)}}" method="POST" enctype="multipart/form-data">
        @method('PUT')
        @csrf
        <div class="sa-text-field-block">
            <label for="">User Name</label>
            <input type="text" name="name" id="nameid" placeholder="Enter User Name" value="{{old('name', $user->name)}}">
        </div>
        <div class="sa-text-field-block">
            <label for="">Email</label>
            <input type="email" name="email" id="emailid" placeholder="Enter Email Address" value="{{old('email', $user->email)}}">
        </div>
        <div class="sa-text-field-block">
            <label for="phoneid">Phone Number</label>
            <input type="text" name="phonenumber" id="phoneid" placeholder="Enter user phone number" value="{{ old('phonenumber', $user->phonenumber) }}">
        </div>
        <div class="sa-text-field-block">
            <label for="">Password</label>
            <input type="password" name="password" id="passwordid" placeholder="Enter New Password (leave blank to keep current)">
        </div>
        <div class="sa-text-field-block">
            <label for="password_confirmation">Confirm Password</label>
            <input type="password" name="password_confirmation" id="password_confirmation" placeholder="Confirm password">
        </div>
        {{-- <div class="sa-text-field-block">
            <label for="">Role</label>
            <select name="role" id="roleid">
                <option value="" disabled>Select Role</option>
                <option value="admin" {{$user->role == 'admin' ? 'selected' : ''}}>Admin</option>
                <option value="user" {{$user->role == 'user' ? 'selected' : ''}}>User</option>
            </select>
        </div> --}}
        <div class="sa-form-button">
            <input type="submit" value="Update">
        </div>
    </form>
</div>
@endsection
