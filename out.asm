.data
	new_line: .asciiz "\n"
	varsum: .word 0
	vari: .word 0
	vary: .word 0
	varx: .word 0
.text
.globl main
main:
	li $v0, 2
	sw $v0, varx
	la $t0, varx
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 3
	subu $sp, $sp, 4
	sw $v0, ($sp)
	la $t0, varx
	lw $v0, ($t0)
	lw $t0, ($sp)
	addu $sp, $sp, 4
	mult $t0, $v0
	mflo $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	add $v0, $t0, $v0
	sw $v0, vary
	la $t0, varx
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp)
	la $t0, vary
	lw $v0, ($t0)
	lw $t0, ($sp)
	addu $sp, $sp, 4
	add $v0, $t0, $v0
	sw $v0, varx
	la $t0, varx
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp)
	la $t0, vary
	lw $v0, ($t0)
	lw $t0, ($sp)
	addu $sp, $sp, 4
	mult $t0, $v0
	mflo $v0
	move $a0, $v0
	li $v0, 1
	syscall
	la $a0, new_line
	li $v0, 4
	syscall
	la $t0, varx
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp)
	la $t0, vary
	lw $v0, ($t0)
	lw $t0, ($sp)
	addu $sp, $sp, 4
	ble $t0, $v0, endif1
	la $t0, varx
	lw $v0, ($t0)
	move $a0, $v0
	li $v0, 1
	syscall
	la $a0, new_line
	li $v0, 4
	syscall
	la $t0, vary
	lw $v0, ($t0)
	move $a0, $v0
	li $v0, 1
	syscall
	la $a0, new_line
	li $v0, 4
	syscall
endif1:
	li $v0, 0
	sw $v0, varx
while1:
	la $t0, varx
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 8
	lw $t0, ($sp)
	addu $sp, $sp, 4
	bge $t0, $v0, term_while1
	la $t0, varx
	lw $v0, ($t0)
	move $a0, $v0
	li $v0, 1
	syscall
	la $a0, new_line
	li $v0, 4
	syscall
	la $t0, varx
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 2
	lw $t0, ($sp)
	addu $sp, $sp, 4
	add $v0, $t0, $v0
	sw $v0, varx
	j while1
term_while1:
	li $v0, 0
	sw $v0, varsum
	li $v0, 1
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 1
	lw $t0, ($sp)
	addu $sp, $sp, 4
	sub $v0, $t0, $v0
	sw $v0, vari
	lw $t1, vari
	subu $sp, $sp, 4
	sw $t1, ($sp)
	li $t2, 5
	subu $sp, $sp, 4
	sw $t2, ($sp)
for2:
	lw $t2, ($sp)
	addu $sp, $sp, 4
	lw $t1, ($sp)
	addu $sp, $sp, 4
	addiu $t1, $t1, 1
	sw $t1, vari
	bgt $t1, $t2, term_for2
	subu $sp, $sp, 4
	sw $t1, ($sp)
	subu $sp, $sp, 4
	sw $t2, ($sp)
	la $t0, varsum
	lw $v0, ($t0)
	subu $sp, $sp, 4
	sw $v0, ($sp)
	la $t0, vari
	lw $v0, ($t0)
	lw $t0, ($sp)
	addu $sp, $sp, 4
	add $v0, $t0, $v0
	sw $v0, varsum
	la $t0, varsum
	lw $v0, ($t0)
	move $a0, $v0
	li $v0, 1
	syscall
	la $a0, new_line
	li $v0, 4
	syscall
	j for2
term_for2:
	li $v0 10
	syscall

