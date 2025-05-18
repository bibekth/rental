
@extends('layouts.app')
@section('content')
<div class="msjk-table-list-listing">
    <h1 class="my-4">Comment  Details</h1>
    @if (Session::has('success'))
    <div class="alert alert-success" role="alert">
      {{ Session::get('success') }}
    </div>
        
    @endif

    <div class="table-responsive msjk-table-body">
        <table class="table msjk-customize-table table-hover">
        <thead>
          <tr class="custom-class-tr">
            
            <th>Username</th>
            <th>Product Name</th>
            <th>Comment</th>
            <th>Posted At</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
            @forelse ($comments as $comment)
                <tr>
                    <td>{{ $comment->user->name }}</td>
                  <td>{{$comment->product->name}}</td>
                    <td>{{ $comment->content }}</td>
                    <td>{{ $comment->created_at }}</td>
                    <td class="action-custom-msjk">
                        {{-- <a href="{{ route('comment.edit',$comment->id) }}"><i class="fas fa-edit" style="color: #000;"></i></a> --}}
                        <form action="{{route('comment.destroy',$comment->id)}}" method="POST"  class="confirm-delete">
                          @method('DELETE')
                          @csrf
                          <button><i class="fas fa-trash" style="color: red;"></i></button>
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
<script>
  document.addEventListener('DOMContentLoaded', function() {
      // Attach an event listener to all forms with the class "confirm-delete"
      const deleteForms = document.querySelectorAll('form.confirm-delete');

      deleteForms.forEach(form => {
          form.addEventListener('submit', function(e) {
              // Show a confirmation dialog
              if (!confirm('Are you sure you want to delete this item?')) {
                  e.preventDefault(); // Prevent the form from submitting if the user clicks 'Cancel'
              }
          });
      });
  });
</script>