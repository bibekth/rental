@extends('layouts.app')

@section('content')
<div class="msjk-container mt-3">
    <h1 class="msjk-singleproduct-heading">
        {{ $user->name }} Details</h1>

    <div class="card">
        <div class="card-body">
            <div class="row align-items-center">
                <div class="msjk-singleproduct-desc col-md-6">
                    <p><strong>Name:</strong> {{ $user->name }}</p>
                    <p><strong>Email:</strong> {{ $user->email }}</p>
                    <p><strong>Phone Number:</strong> {{ $user->phonenumber }}</p>
                    {{-- <p><strong>Role:</strong> {{ ucfirst($user->role) }}</p> --}}
                </div>
                {{-- <div class="msjk-single-image col-md-6">
                    @if($user->profile_photo)
                        <img src="{{ asset('storage/' . $user->profile_photo) }}" alt="{{ $user->name }}" class="img-fluid">
                    @else
                        <p>No profile photo available</p>
                    @endif
                </div> --}}
            </div>
            <div class="msjk-edit-del-back-content d-flex">
                <a href="{{ route('user.index') }}" class="btn btn-secondary mt-3">Back to List</a>
                <a href="{{ route('user.edit', $user->id) }}" class="btn btn-primary mt-3">Edit User</a>

                <form action="{{ route('user.destroy', $user->id) }}" method="POST" class="mt-3">
                    @method('DELETE')
                    @csrf
                    <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this user?')">Delete User</button>
                </form>
            </div>
        </div>
    </div>
</div>
@endsection
