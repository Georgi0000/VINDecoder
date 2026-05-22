package com.example.repository

import com.example.model.IntervalType
import com.example.model.ManufacturerInfo
import com.example.model.ServiceInterval
//in-memory storage for our initial manufacturers and their specific service intervals mapped
//to ServiceInterval()
object VinIntervalRepository {

    // Decodes the first three characters of a VIN (case-insensitive)
    //
    fun decodeVin(vin: String): ManufacturerInfo {
        if (vin.length < 3) return getGenericManufacturer("Unknown")
        
        val wmi = vin.take(3).uppercase()

        return when {
            // Mercedes-Benz
            wmi in listOf("WDB", "WD1", "WD2", "WD3", "WD4", "WD8", "WDF") -> getMercedesBenz()
            
            // BMW
            wmi in listOf("WBA", "WBS", "5YM") -> getBMW()
            
            // Audi
            wmi in listOf("WAU", "WA1", "TRU") -> getAudi()
            
            // Volkswagen
            wmi in listOf("WVW", "WVG", "1V2", "3VW") -> getVolkswagen()
            
            // Tesla
            wmi in listOf("5YJ", "LRW", "7SA") -> getTesla()
            
            // Toyota
            wmi in listOf("JTD", "JT2", "JT1", "JTM", "4T1", "5TB") -> getToyota()
            
            // Honda
            wmi in listOf("JH4", "1HG", "JHM", "5FN") -> getHonda()
            
            // Ford
            wmi in listOf("1FA", "1FT", "1FM", "3FA", "2FM") -> getFord()
            
            // Porsche
            wmi == "WP0" -> getPorsche()
            
            // Mazda
            wmi in listOf("JM1", "JM0", "1YV") -> getMazda()
            
            // Hyundai/Kia
            wmi in listOf("KMH", "KNA", "KND") -> getHyundaiKia()
            
            // General / Fallback
            else -> getGenericManufacturer(wmi)
        }
    }

    private fun getMercedesBenz() = ManufacturerInfo(
        name = "Mercedes-Benz",
        logoAccentChar = "⨂", // representation of the star
        intervals = listOf(
            ServiceInterval("General Oil Interval", 15000, "Recommended high-grade synthetic engine oil fluid exchange", IntervalType.OIL_CHANGE),
            ServiceInterval("Front Brake Pads", 35000, "Inspect wear indicators & replacement of front ceramic friction pads", IntervalType.BRAKES_FRONT),
            ServiceInterval("Rear Brake Pads", 45000, "Inspect callipers and exchange rear ceramic friction pads", IntervalType.BRAKES_REAR),
            ServiceInterval("Cabin Air Filter", 25000, "Charcoal-activated filter refresh for optimal occupant air quality", IntervalType.CABIN_FILTER),
            ServiceInterval("Engine Air Intake Filter", 45000, "Optimal turbo/intake respiration clean and filter replacement", IntervalType.ENGINE_FILTER),
            ServiceInterval("Double Platinum Spark Plugs", 75000, "Maintain cylinder pressure & ignition efficiency upgrade", IntervalType.SPARK_PLUGS),
            ServiceInterval("Transmission Fluid & Gasket", 80000, "Gear wear reduction & smooth shifting automatic transmission flush", IntervalType.TRANSMISSION_FLUID),
            ServiceInterval("Premium Coolant Fluid", 150000, "High duration blue coolant solution replacement to protect engine block", IntervalType.COOLANT_FLUSH),
            ServiceInterval("Synthetic Brake Fluid", 40000, "Moisture-purged DOT4 reservoir flush (Recommended every 2 years)", IntervalType.BRAKE_FLUID)
        )
    )

    private fun getBMW() = ManufacturerInfo(
        name = "BMW (Bayerische Motoren Werke)",
        logoAccentChar = "Ⓜ",
        intervals = listOf(
            ServiceInterval("Severe Duty Oil Interval", 10000, "Required high-purity LL-01 synthetic engine oil and premium filter", IntervalType.OIL_CHANGE),
            ServiceInterval("Premium Front Brake Pads", 30000, "Exchange pads and electronic wear sensors", IntervalType.BRAKES_FRONT),
            ServiceInterval("Premium Rear Brake Pads", 40000, "Exchange rear discs lining & wear sensors", IntervalType.BRAKES_REAR),
            ServiceInterval("Microfilter Cabin ventilation", 20000, "Allergy charcoal active particulate element replacement", IntervalType.CABIN_FILTER),
            ServiceInterval("High-Flow Intake Filter", 40000, "Engine air filter element replacement to guard turbo compressors", IntervalType.ENGINE_FILTER),
            ServiceInterval("Iridium Spark Plugs", 60000, "Precise coil ignition spark plugs exchange to prevent misfires", IntervalType.SPARK_PLUGS),
            ServiceInterval("LongLife Automatic Gearbox Fluid", 80000, "ZF 8-speed fluid and integrated pan-filter service", IntervalType.TRANSMISSION_FLUID),
            ServiceInterval("Low Moisture Brake Fluid", 30000, "Hydraulic brake fluid bleed-out and cylinder cleanse (every 2 years)", IntervalType.BRAKE_FLUID)
        )
    )

    private fun getAudi() = ManufacturerInfo(
        name = "Audi (VAG High-Output)",
        logoAccentChar = "◯",
        intervals = listOf(
            ServiceInterval("Spec Synthetic Oil Interval", 15000, "Approved VW 504.00/507.00 low ash engine oil change", IntervalType.OIL_CHANGE),
            ServiceInterval("Front Friction Brake Pads", 35000, "Billion-grade semi-metallic pads standard check and assembly", IntervalType.BRAKES_FRONT),
            ServiceInterval("Rear Friction Brake Pads", 45000, "Rear electronic parking brake caliper service and pad change", IntervalType.BRAKES_REAR),
            ServiceInterval("Dust & Pollen Cabin Filter", 30000, "Pre-cabin active odor isolation filter replacement", IntervalType.CABIN_FILTER),
            ServiceInterval("Combustion Intake Filter", 60000, "Engine respiration intake protection cover cleaning & service", IntervalType.ENGINE_FILTER),
            ServiceInterval("High Performance Spark Plugs", 60000, "TFSI direct-injection optimized thermal rating spark plugs", IntervalType.SPARK_PLUGS),
            ServiceInterval("S-Tronic DSG Clutch Fluid", 60000, "Dual-clutch transmission internal gear fluid change", IntervalType.TRANSMISSION_FLUID),
            ServiceInterval("DOT4 Brake Fluid Flush", 40000, "Full system pressure purging of hydraulic brake lines", IntervalType.BRAKE_FLUID)
        )
    )

    private fun getVolkswagen() = ManufacturerInfo(
        name = "Volkswagen (VW Group)",
        logoAccentChar = "Ⓥ",
        intervals = listOf(
            ServiceInterval("Certified Engine Oil Change", 15000, "Full synthetic VW 502.00/505.00 spec oil service", IntervalType.OIL_CHANGE),
            ServiceInterval("Front Brake Pads", 35000, "Squeak-free compound pad installation and slide pen lubrication", IntervalType.BRAKES_FRONT),
            ServiceInterval("Rear Brake Pads", 45000, "Fitted rear compound friction pads replacement", IntervalType.BRAKES_REAR),
            ServiceInterval("Odor Blocking Cabin Filter", 30000, "Cabin HVAC interior environment air filter exchange", IntervalType.CABIN_FILTER),
            ServiceInterval("Engine Air Filter", 60000, "Aspirator filter element replacement to protect engine pistons", IntervalType.ENGINE_FILTER),
            ServiceInterval("Quad-Electrode Spark Plugs", 60000, "Ignition coil-over service and spark plugs swap", IntervalType.SPARK_PLUGS),
            ServiceInterval("Brake Fluid Purge", 40000, "Brake pressure stabilization fluid replacement (2-year rule)", IntervalType.BRAKE_FLUID)
        )
    )

    private fun getTesla() = ManufacturerInfo(
        name = "Tesla (Electric Vehicle)",
        logoAccentChar = "Ⓣ",
        intervals = listOf(
            ServiceInterval("HEPA Cabin Air Filters", 20000, "Tesla Bioweapon Defense charcoal cabin air filters upgrade", IntervalType.CABIN_FILTER),
            ServiceInterval("Front & Rear Brake Pads", 100000, "Extremely durable brake lifecycle due to regenerative motor braking. Clean/Lube calipers yearly in cold climates.", IntervalType.BRAKES_FRONT),
            ServiceInterval("AC Desiccant Bag Flush", 80000, "Air conditioning receiver-dryer moisture desiccant cartridge swap", IntervalType.COOLANT_FLUSH),
            ServiceInterval("Brake Fluid Water Check", 40000, "Brake fluid moisture content measurement and flush if safety rating is compromised", IntervalType.BRAKE_FLUID)
        )
    )

    private fun getToyota() = ManufacturerInfo(
        name = "Toyota Motor Corporation",
        logoAccentChar = "⏀",
        intervals = listOf(
            ServiceInterval("LongLife Engine Oil", 10000, "0W-20 low-viscosity high-purity synthetic oil and filter", IntervalType.OIL_CHANGE),
            ServiceInterval("Standard Front Brake Pads", 45000, "Front calliper slider pins cleanup and heavy duty pad swap", IntervalType.BRAKES_FRONT),
            ServiceInterval("Standard Rear Brake Pads", 55000, "Rear solid-back organic brake pads check & replacement", IntervalType.BRAKES_REAR),
            ServiceInterval("Allergen Cabin Filter", 20000, "Pollen isolation paper screen refresh", IntervalType.CABIN_FILTER),
            ServiceInterval("Engine Air Intake Filter", 40000, "Pleated-structure air filter replacement for efficient combustion", IntervalType.ENGINE_FILTER),
            ServiceInterval("Iridium Tough Spark Plugs", 100000, "Super extended lifecycle ignition element swap", IntervalType.SPARK_PLUGS),
            ServiceInterval("Super Long Life Coolant", 160000, "Factory pink low-conductivity custom coolant solution replace", IntervalType.COOLANT_FLUSH)
        )
    )

    private fun getHonda() = ManufacturerInfo(
        name = "Honda Motor Company",
        logoAccentChar = "Ⓗ",
        intervals = listOf(
            ServiceInterval("VTEC Engine Oil Service", 10000, "Premium low-drag synthetic oil and spin-on filter swap", IntervalType.OIL_CHANGE),
            ServiceInterval("Front Metallic Brake Pads", 40000, "Replacement of front disc brake friction components", IntervalType.BRAKES_FRONT),
            ServiceInterval("Rear Metallic Brake Pads", 50000, "Rear brake assembly overhaul and pad adjustment", IntervalType.BRAKES_REAR),
            ServiceInterval("Deodorizing Cabin Filter", 25000, "HVAC environmental dust screen exchange", IntervalType.CABIN_FILTER),
            ServiceInterval("High Respiration Intake Filter", 45000, "Optimal engine combustion air supply filter swap", IntervalType.ENGINE_FILTER),
            ServiceInterval("Laser Iridium Spark Plugs", 90000, "High energy spark plugs replace to keep VTEC combustion sharp", IntervalType.SPARK_PLUGS),
            ServiceInterval("Brake Fluid Hydration Purge", 45000, "Purge air and accumulated moisture from lines", IntervalType.BRAKE_FLUID)
        )
    )

    private fun getFord() = ManufacturerInfo(
        name = "Ford Motor Company (EcoBoost / SuperDuty)",
        logoAccentChar = "Ⓕ",
        intervals = listOf(
            ServiceInterval("Motorcraft Fully Synthetic Oil", 12000, "High shear synthetic oil change for turbocharged engines", IntervalType.OIL_CHANGE),
            ServiceInterval("Front Heavy Duty Pads", 40000, "Towing-rated high heat threshold brake pad set replacement", IntervalType.BRAKES_FRONT),
            ServiceInterval("Rear Heavy Duty Pads", 50000, "Rear load bearing drum-brake adjusting or disc pad swap", IntervalType.BRAKES_REAR),
            ServiceInterval("Odour Filtering Cabin Screen", 25000, "Pleated cabin charcoal ventilation membrane replacement", IntervalType.CABIN_FILTER),
            ServiceInterval("High Density Engine Intake Filter", 45000, "Combustion dust protection screen filter refreshment", IntervalType.ENGINE_FILTER),
            ServiceInterval("High-Output Spark Plugs", 90000, "Turbo-adjusted spark plugs gap review and exchange", IntervalType.SPARK_PLUGS)
        )
    )

    private fun getPorsche() = ManufacturerInfo(
        name = "Porsche (Stuttgart Performance)",
        logoAccentChar = "Ⓟ",
        intervals = listOf(
            ServiceInterval("Mobil 1 Sport Synthetic Oil", 10000, "High thermal stress tolerance performance oil and dry-sump filter swap", IntervalType.OIL_CHANGE),
            ServiceInterval("Brembo Front Ceramic Pads", 30000, "Track-tested structural compound pad swap and caliper check", IntervalType.BRAKES_FRONT),
            ServiceInterval("Brembo Rear Ceramic Pads", 40000, "Rear racing-spec caliper lining and sensor replacement", IntervalType.BRAKES_REAR),
            ServiceInterval("Engine Dust Air Intake System", 30000, "Twin-turbo side-intake air elements replacement", IntervalType.ENGINE_FILTER),
            ServiceInterval("Laser Platinum Spark Plugs", 40000, "Performance-rated engine ignition plugs replacement", IntervalType.SPARK_PLUGS)
        )
    )

    private fun getMazda() = ManufacturerInfo(
        name = "Mazda (SkyActiv Drive)",
        logoAccentChar = "Ⓜ",
        intervals = listOf(
            ServiceInterval("SkyActiv Low-摩擦 Synthetic Oil", 10000, "Mazda genuine high-lubricity synthetic oil change", IntervalType.OIL_CHANGE),
            ServiceInterval("Front Brake Pads & Cleansing", 40000, "Friction pads update and sensor caliper lubrication", IntervalType.BRAKES_FRONT),
            ServiceInterval("Rear Brake Pads & Cleansing", 50000, "Rear linear brake linings standard assessment", IntervalType.BRAKES_REAR),
            ServiceInterval("Cabin Microfilter Replacement", 20000, "HVAC active cabin air screen cleaning and swapping", IntervalType.CABIN_FILTER)
        )
    )

    private fun getHyundaiKia() = ManufacturerInfo(
        name = "Hyundai-Kia Group",
        logoAccentChar = "Ⓗ",
        intervals = listOf(
            ServiceInterval("Clean-Shield Synthetic Oil", 10000, "Highly stable multi-grade synthetic engine oil update", IntervalType.OIL_CHANGE),
            ServiceInterval("Front Brake Pad Retainers", 40000, "Installation of silent-compound organic front brake pads", IntervalType.BRAKES_FRONT),
            ServiceInterval("Rear Brake Pad Retainers", 50000, "Trailing-arm brake hardware check and pad replacement", IntervalType.BRAKES_REAR),
            ServiceInterval("Cabin Dust Guard Filter", 20000, "A/C duct particulate mesh screen replacement", IntervalType.CABIN_FILTER),
            ServiceInterval("Engine Breathing Filter Element", 40000, "Engine filtration element replacement to ensure intake seal", IntervalType.ENGINE_FILTER)
        )
    )

    private fun getGenericManufacturer(prefix: String) = ManufacturerInfo(
        name = "General Automotive standard (Prefix: $prefix)",
        logoAccentChar = "⚙",
        intervals = listOf(
            ServiceInterval("General Motor Oil", 12000, "Standard viscosity full synthetic passenger car engine oil", IntervalType.OIL_CHANGE),
            ServiceInterval("Standard Front Brake Pads", 35000, "Front organic/ceramic safety-rated brake friction swap", IntervalType.BRAKES_FRONT),
            ServiceInterval("Standard Rear Brake Pads", 45000, "Rear disc brake pads wear checks & service", IntervalType.BRAKES_REAR),
            ServiceInterval("Standard Cabin Odor Filter", 25000, "A/C cabin fresh air particulate element replacement", IntervalType.CABIN_FILTER),
            ServiceInterval("Combustion Air Filter", 45000, "Intake air compartment maintenance filter replacement", IntervalType.ENGINE_FILTER),
            ServiceInterval("Extended Wear Spark Plugs", 75000, "General ignition plug replacement and connection wire check", IntervalType.SPARK_PLUGS),
            ServiceInterval("Brake Fluid Moisture Flush", 40000, "Hydraulic brake oil quality check and bleed-down", IntervalType.BRAKE_FLUID)
        )
    )
}
