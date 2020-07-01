package io.github.jsnimda.inventoryprofiles.item.rule.parameter

import io.github.jsnimda.common.util.ByPropertyName
import io.github.jsnimda.inventoryprofiles.item.rule.ArgumentType
import io.github.jsnimda.inventoryprofiles.item.rule.Parameter
import java.text.Collator

// ============
// Some helper functions for creating parameters
// ============

private fun <T : Any> param(argumentType: ArgumentType<T>) = ByPropertyName<Parameter<T>> { name ->
  Parameter(name, argumentType).also { PARAMETER_MAP[name] = it } // don't use NativeParameters.map
}

private inline fun <reified T : Enum<T>> enum() = param(
  EnumArgumentType(
    T::class.java
  )
)

private val any_string     /**/ = param(StringArgumentType)
private val type_boolean   /**/ = param(BooleanArgumentType)
private val any_nbt        /**/ = param(NbtArgumentType)
private val any_rule       /**/ = param(RuleArgumentType)
private val any_tag_name   /**/ = param(TagNameArgumentType)
private val any_item_name  /**/ = param(ItemNameArgumentType)
private val any_nbt_path   /**/ = param(NbtPathArgumentType)

// for .* imports

object NativeParameters {
  val map = PARAMETER_MAP // force load class
}

private val PARAMETER_MAP = mutableMapOf<String, Parameter<*>>()

val string_compare           /**/ by enum<StringCompare>()
val locale                   /**/ by any_string
val strength                 /**/ by enum<Strength>()
val logical                  /**/ by type_boolean
val match                    /**/ by enum<Match>()
val has_custom_name          /**/ by param(match.argumentType)
val number_order             /**/ by enum<NumberOrder>()
val sub_rule_match           /**/ by any_rule
val sub_rule_not_match       /**/ by any_rule
val nbt                      /**/ by any_nbt
val allow_extra              /**/ by type_boolean
val require_nbt              /**/ by enum<RequireNbt>()
val tag_name                 /**/ by any_tag_name
val item_name                /**/ by any_item_name
val nbt_path                 /**/ by any_nbt_path
val has_nbt_path             /**/ by param(match.argumentType)
val reverse                  /**/ by type_boolean
val sub_rule                 /**/ by any_rule
val has_potion_name          /**/ by param(match.argumentType)
val has_potion_effects       /**/ by param(match.argumentType)

enum class StringCompare(val comparator: Comparator<in String>?) {
  UNICODE(naturalOrder()),
  IGNORE_CASE(String.CASE_INSENSITIVE_ORDER),
  LOCALE(null)
}

enum class Strength(val value: Int) {
  PRIMARY(Collator.PRIMARY),
  SECONDARY(Collator.SECONDARY),
  TERTIARY(Collator.TERTIARY),
  IDENTICAL(Collator.IDENTICAL)
}

enum class NumberOrder(private val comparator: Comparator<in Double>) : Comparator<Number> {
  ASCENDING(naturalOrder()),
  DESCENDING(reverseOrder());

  override fun compare(num1: Number, num2: Number): Int =
    comparator.compare(num1.toDouble(), num2.toDouble())
}

enum class Match(val multiplier: Int) {
  FIRST(1),
  LAST(-1)
}

enum class RequireNbt {
  REQUIRED,
  NO_NBT,
  NOT_REQUIRED
}

