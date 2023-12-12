.data
	null: .word 0	# null variable
	error_message: .asciiz "RuntimeError: null assignment, program quit unexpectedly"	# error message
	new_line: .asciiz "\n"	# new line variable
.text
.globl main
main:
	li $v0, 5	# load the number $v0
	addi $sp, $sp, -4
	sw $v0, ($sp)
	jal procfact	# call procedure
	lw $t0, ($sp)
	addu $sp, $sp, 4
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
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
	lw $ra, ($sp)
	addu $sp, $sp, 4
	lw $v0, ($sp)
	addu $sp, $sp, 4
	jr $ra	# return
	lw $v0, 0($sp)	# load the variable into $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
	lw $ra, ($sp)
	addu $sp, $sp, 4
	lw $v0, ($sp)
	addu $sp, $sp, 4
	jr $ra	# return

