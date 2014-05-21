package fr.unice.modalis.cosmic.exceptions

import fr.unice.modalis.cosmic.core.Node

class NodeNotFoundException(n: Node) extends Exception("Node " + n + " not referended") {

}