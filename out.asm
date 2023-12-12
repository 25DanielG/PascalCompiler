.data
	null: .word 0	# null variable
	error_message: .asciiz "RuntimeError: null assignment, program quit unexpectedly"	# error message
	new_line: .asciiz "\n"	# new line variable
	varx: .word 0
.text
.globl main
main:
	li $v0, 5	# load the number $v0
	addi $sp, $sp, -4
	sw $v0, ($sp)
	jal procfact	# call procedure
	lw $t0, ($sp)
	addu $sp, $sp, 4
	la $t3, null	# load address of null variable
	beq $v0, $t3, program_error	# error if assigning null
	sw $v0, varx	# assign the variable
	la $t0, varx
	lw $v0, ($t0)	# load the variable into $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
program_exit:
	li $v0 10	# exit the program
	syscall
program_error:
	li $v0 4	# print error message
	la $a0 error_message
	syscall
	j program_exit
procfact:
	la $t0, null
	move $v0, $t0	# load null into $v0
	addi $sp, $sp, -4
	sw $v0, ($sp)
	addi $sp, $sp, -4
	sw $ra, ($sp)
	lw $v0, 8($sp)	# load the variable into $v0
	addi $sp, $sp, -4
	sw $v0, ($sp)
	li $v0, 1	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	bge $t0, $v0, elseif2	# if $t0 is greater than or equal to $v0
	li $v0, 1	# load the number $v0
	sw $v0, 4($sp)	# assign the variable
	j endif1	# jump to end of if statement
elseif2:
	lw $v0, 8($sp)	# load the variable into $v0
	addi $sp, $sp, -4
	sw $v0, ($sp)
	li $v0, 1	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	sub $v0, $t0, $v0	# subtract the two expressions
	addi $sp, $sp, -4
	sw $v0, ($sp)
	jal procfact	# call procedure
	lw $t0, ($sp)
	addu $sp, $sp, 4
	addi $sp, $sp, -4
	sw $v0, ($sp)
	lw $v0, 12($sp)	# load the variable into $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	mult $t0, $v0	# add the multiply expressions
	mflo $v0
	sw $v0, 4($sp)	# assign the variable
endif1:
	lw $ra, ($sp)
	addu $sp, $sp, 4
	lw $v0, ($sp)
	addu $sp, $sp, 4
	jr $ra	# return

