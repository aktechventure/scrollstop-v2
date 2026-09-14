package com.aswinkumar.scrollstop.domain.engine

class PatternEngine(
    private val monitoredPackages: Set<String>
) {
    fun isMonitored(packageName: String): Boolean = packageName in monitoredPackages
}
