package com.codacy.analysis.cli.formatter

import java.io.{FileOutputStream, PrintStream}

import better.files.File
import com.codacy.analysis.core.model.Result
import org.log4s.{Logger, getLogger}

trait FormatterCompanion {
  def name: String
  def apply(stream: PrintStream): Formatter
}

trait Formatter {

  def stream: PrintStream

  def begin(): Unit

  def add(element: Result): Unit

  def addAll(elements: Seq[Result]): Unit = elements.foreach(add)

  def end(): Unit

}

object Formatter {

  private val logger: Logger = getLogger

  private val defaultPrintStream = Console.out

  val defaultFormatter: FormatterCompanion = Text

  val allFormatters: Set[FormatterCompanion] = Set(defaultFormatter, Json)

  def apply(name: String,
            file: Option[File] = Option.empty,
            printStream: Option[PrintStream] = Option.empty): Formatter = {

    val builder = allFormatters.find(_.name.equalsIgnoreCase(name)).get

    val stream = file.map(asPrintStream).orElse(printStream).getOrElse(defaultPrintStream)

    builder(stream)
  }

  private def asPrintStream(file: File) = {
    new PrintStream(new FileOutputStream(file.toJava, false))
  }
}
