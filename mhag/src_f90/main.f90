program mhag_main
   implicit none

   call init
   call mhag_proc

end program mhag_main

subroutine init
   use mhag
   implicit none
   integer :: num_list(9),slots(300,9),i,j,k
   character(len=40) :: menus(300,9)

   call read_arg
   call mhag_intro
   call read_data
   call data_pre_proc

!  call gen_menu_list(.false.,.true.,num_list,menus,slots)
!   do i=1,9
!      do j=1,num_list(i)
!         print "(2I4,4X,A40,I4)",i,j,menus(j,i),slots(j,i)
!      enddo
!   enddo
!   stop

end subroutine init

subroutine mhag_proc
   use mhag
   implicit none
   type(set_type) :: armor_set

   ! process input file
   select case (method)
   case (1)  ! single calculator
      call mhag_input_cal(armor_set)
      call armor_calculator(armor_set)
      call armor_output(armor_set)

   case (2)  ! batach calculator
      call mhag_cal_batch

   case (3)  ! generator
!     call mhag_input_gen(armor_set)

   end select

   call mhag_clean

end subroutine mhag_proc
