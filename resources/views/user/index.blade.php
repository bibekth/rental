@extends('layouts.app')
@section('content')
<div class="msjk-table-list-listing">
    <h1 class="my-4">Users</h1>
    <div class="msjk-create-btn">
        <a href="{{route('user.create')}}">Create User</a>
    </div>
    @if (Session::has('success'))
    <div class="alert alert-success" role="alert">
      {{ Session::get('success') }}
    </div>
    @endif

    <div class="table-responsive msjk-table-body">
        <table class="table msjk-customize-table table-hover">
        <thead>
          <tr class="custom-class-tr">
            <th scope="col">Name</th>
            <th scope="col">Email</th>
            <th scope="col">Phone Number</th>
            {{-- <th scope="col">Role</th> --}}
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
            @forelse ($users as $user)
            <tr>
                <td>{{$user->name}}</td>
                <td>{{$user->email}}</td>
                <td>{{ $user->phonenumber }}</td>
                {{-- <td>{{$user->role}}</td> --}}
                <td class="action-custom-msjk">
                    <a href="{{ route('user.show', $user->id) }}"><i class="fas fa-eye" style="color: #000;"></i></a>
                    <a href="{{ route('user.edit',$user->id) }}"><i class="fas fa-edit" style="color: #000;"></i></a>
                    <form action="{{route('user.destroy',$user->id)}}" method="POST" class="confirm-delete">
                      @method('DELETE')
                      @csrf
                      <button onclick="return confirm('Are you sure you want to delete this user?')"><i class="fas fa-trash" style="color: red;"></i></button>
                    </form>
                </td>
              </tr>  
            @empty
              <td>No Data Found</td>  
            @endforelse
        </tbody>
      </table>
    </div>
  </div>
@endsection
