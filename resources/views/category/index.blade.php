@extends('layouts.app')
@section('content')
<div class="msjk-table-list-listing">
    <h1 class="my-4">Category</h1>
    <div class="msjk-create-btn">
        <a href="{{route('category.create')}}">Create Category</a>
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
            
            {{-- <th scope="col">SN</th> --}}
            <th scope="col">Name</th>
            <th scope="col">Photo</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
            @forelse ($categories as $category)
            <tr>
                <td>{{$category->name}}</td>
                <td><img src="{{asset('storage/'.$category->photo)}}" alt="" width="50px" height="50px"></td>
                <td class="action-custom-msjk">
                  <a href="{{ route('category.show',$category->id) }}"><i class="fas fa-eye" style="color: #000; font-size: 16px;"></i></a>
                  <a href="{{ route('category.edit',$category->id) }}"> <i class="fas fa-edit" style="color: #000; font-size: 16px;"></i></a>
                  <form action="{{route('category.destroy',$category->id)}}" method="POST"  class="confirm-delete">
                    @method('DELETE')
                    @csrf
                    <button onclick="return confirm('Are you sure you want to delete this item?')"><i class="fas fa-trash" style="color: red;"></i></button>
                  </form>
              </td>
            
                
              </tr>  
            @empty
              <td>No Data Found</td>  
            @endforelse
          
         
        
      </table>
  
    </div>
    
  </div>

@endsection
