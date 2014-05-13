package fr.unice.modalis.fsm.exceptions

import fr.unice.modalis.fsm.core.Node

class NodeNotFoundException(n: Node) extends Exception("Node " + n + " not referended") {

}