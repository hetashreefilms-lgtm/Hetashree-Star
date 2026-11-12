package com.example.data

import com.example.model.*

object MockInitialData {

    val adminUser = User(
        id = "USR-ADMIN-01",
        email = "hetashreefilms@gmail.com",
        name = "HETASHREE Administration",
        username = "hetashree_official",
        role = UserRole.SUPER_ADMIN,
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
        isVerified = true,
        companyName = "HETASHREE FILM'S PRODUCTION"
    )

    val producerUser = User(
        id = "USR-PROD-01",
        email = "vikram.malhotra@dharmavision.com",
        name = "Vikram Malhotra",
        username = "vikram_producer",
        role = UserRole.PRODUCER,
        avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
        isVerified = true,
        companyName = "Dharma Vision Productions"
    )

    val directorUser = User(
        id = "USR-DIR-01",
        email = "kabir.mehta@velvetmotion.com",
        name = "Kabir Mehta",
        username = "kabir_director",
        role = UserRole.DIRECTOR,
        avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
        isVerified = true,
        companyName = "Velvet Motion Pictures"
    )

    val castingDirectorUser = User(
        id = "USR-CAST-01",
        email = "sunaina@elitecasting.com",
        name = "Sunaina Oberoi",
        username = "sunaina_casting",
        role = UserRole.CASTING_DIRECTOR,
        avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400",
        isVerified = true,
        companyName = "HETASHREE CASTING & TALENT"
    )

    val priyaUser = User(
        id = "USR-ART-001",
        email = "priya.sharma@talent.in",
        name = "Priya Sharma",
        username = "priyasharma_actor",
        role = UserRole.ARTIST,
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500",
        isVerified = true,
        artistIdCode = "HT-ART-000001"
    )

    val aryanUser = User(
        id = "USR-ART-002",
        email = "aryan.kapoor@talent.in",
        name = "Aryan Kapoor",
        username = "aryankapoor_official",
        role = UserRole.ARTIST,
        avatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=500",
        isVerified = true,
        artistIdCode = "HT-ART-000002"
    )

    val ananyaUser = User(
        id = "USR-ART-003",
        email = "guardian.ananya@gmail.com",
        name = "Ananya Roy",
        username = "ananyaroy_star",
        role = UserRole.KIDS_ARTIST,
        avatarUrl = "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=500",
        isVerified = true,
        artistIdCode = "HT-ART-000003"
    )

    val initialProfiles = listOf(
        ArtistProfile(
            artistIdCode = "HT-ART-000001",
            userId = "USR-ART-001",
            fullName = "Priya Sharma",
            username = "priyasharma_actor",
            dateOfBirth = "1998-05-14",
            age = 28,
            gender = "Female",
            primaryCategory = "Lead Artist",
            allCategories = listOf("Lead Artist", "Female Model", "Dancer", "Influencer"),
            currentLocation = "Mumbai, Maharashtra",
            nativePlace = "Jaipur, Rajasthan",
            stateBelonging = "Maharashtra",
            languages = listOf("Hindi", "English", "Marwari", "Punjabi"),
            education = "B.A. in Dramatic Arts, FTII Alumni",
            profession = "Professional Actress & High Fashion Model",
            actingExperienceYears = 5,
            modelingExperienceYears = 6,
            skills = listOf("Classical Kathak", "Method Acting", "Horse Riding", "Stage Combat", "Voice Modulation"),
            specialTalents = listOf("Semi-classical singing", "Dialect adaptability (Rajasthani/Bihari/UP)"),
            biography = "Versatile actress and commercial model with extensive credits across Hindi feature films, top OTT web series, and nationwide TV commercials. Represented exclusively by HETASHREE CASTING & TALENT.",
            portfolio = ArtistPortfolio(
                profilePhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600",
                closeUpPhoto = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600",
                halfPhoto = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600",
                fullLengthPhoto = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=600",
                traditionalPhoto = "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=600",
                westernPhoto = "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=600",
                professionalPhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600",
                modelingPhoto = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=600",
                actingPhoto = "https://images.unsplash.com/photo-1531746020798-e6953c6e8e04?w=600",
                introductionVideos = listOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                auditionVideos = listOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                youtubeLinks = listOf("https://youtube.com/watch?v=priyasharma_monologue")
            ),
            measurements = ArtistMeasurements(
                heightCm = 172,
                heightFeet = "5'8\"",
                weightKg = 54,
                chestInches = 34,
                waistInches = 26,
                hipsInches = 36,
                shoeSize = "UK 6",
                clothingSize = "S / 36",
                hairColor = "Dark Brown",
                eyeColor = "Hazel"
            ),
            verificationStatus = VerificationStatus.APPROVED,
            isVerified = true,
            postsCount = 42,
            fansCount = 18400, // "FANS"
            favesCount = 312,  // "FAVES"
            privateMobile = "+91 98200 45678",
            privateEmail = "priya.official.private@gmail.com",
            privateAddress = "B-402, Sea Green Heights, Bandra West, Mumbai 400050",
            emergencyContact = "+91 98200 11223 (Manager Ramesh)",
            parentGuardianConsent = true
        ),
        ArtistProfile(
            artistIdCode = "HT-ART-000002",
            userId = "USR-ART-002",
            fullName = "Aryan Kapoor",
            username = "aryankapoor_official",
            dateOfBirth = "1995-11-20",
            age = 31,
            gender = "Male",
            primaryCategory = "Character Artist",
            allCategories = listOf("Lead Artist", "Supporting Artist", "Character Artist", "Male Model", "Dancer"),
            currentLocation = "Mumbai, Maharashtra",
            nativePlace = "Chandigarh",
            stateBelonging = "Punjab",
            languages = listOf("Hindi", "English", "Punjabi"),
            education = "Graduate in Mass Media, Barry John Acting Studio",
            profession = "Cinema Actor & Fitness Model",
            actingExperienceYears = 7,
            modelingExperienceYears = 4,
            skills = listOf("Martial Arts", "Hip-Hop / Contemporary Dance", "Action Stunts", "Voice Dubbing"),
            specialTalents = listOf("Acoustic Guitar", "Parkour / Gymnastics"),
            biography = "Charismatic performer known for intense dramatic roles, thrilling action sequences, and high-energy brand campaigns. Featured in multiple national television serials and OTT blockbusters.",
            portfolio = ArtistPortfolio(
                profilePhoto = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600",
                closeUpPhoto = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600",
                halfPhoto = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600",
                fullLengthPhoto = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=600",
                traditionalPhoto = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=600",
                westernPhoto = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600",
                professionalPhoto = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=600",
                modelingPhoto = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=600",
                actingPhoto = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600",
                introductionVideos = listOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                auditionVideos = listOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
                youtubeLinks = listOf("https://youtube.com/watch?v=aryankapoor_actionreel")
            ),
            measurements = ArtistMeasurements(
                heightCm = 183,
                heightFeet = "6'0\"",
                weightKg = 78,
                chestInches = 42,
                waistInches = 32,
                hipsInches = 38,
                shoeSize = "UK 9",
                clothingSize = "L / 42",
                hairColor = "Black",
                eyeColor = "Dark Brown"
            ),
            verificationStatus = VerificationStatus.APPROVED,
            isVerified = true,
            postsCount = 58,
            fansCount = 29500, // "FANS"
            favesCount = 180,  // "FAVES"
            privateMobile = "+91 98111 23456",
            privateEmail = "aryan.private.bookings@gmail.com",
            privateAddress = "Flat 12A, Lokhandwala Complex, Andheri West, Mumbai",
            emergencyContact = "+91 98111 99988 (Brother Rohan)",
            parentGuardianConsent = true
        ),
        ArtistProfile(
            artistIdCode = "HT-ART-000003",
            userId = "USR-ART-003",
            fullName = "Ananya Roy",
            username = "ananyaroy_star",
            dateOfBirth = "2018-09-10",
            age = 8,
            gender = "Kid",
            primaryCategory = "Kids Artists",
            allCategories = listOf("Kids Artists", "Kids Models", "Voice Artists"),
            currentLocation = "Mumbai, Maharashtra",
            nativePlace = "Kolkata, West Bengal",
            stateBelonging = "Maharashtra",
            languages = listOf("Hindi", "English", "Bengali"),
            education = "3rd Grade, Dhirubhai Ambani International School",
            profession = "Child Actor & Commercial Talent",
            actingExperienceYears = 3,
            modelingExperienceYears = 3,
            skills = listOf("Spontaneous Expressions", "Quick Dialogue Memorization", "Singing", "Ballet"),
            specialTalents = listOf("Cartoon voice modulation", "Poetry recitation"),
            biography = "Super adorable and talented child artist with multiple television commercials for leading FMCG brands and a pivotal character in a prominent OTT series. Guided by parents under strict education balance.",
            portfolio = ArtistPortfolio(
                profilePhoto = "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=600",
                closeUpPhoto = "https://images.unsplash.com/photo-1502086223501-7ea6ecd79368?w=600",
                halfPhoto = "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=600",
                fullLengthPhoto = "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?w=600",
                traditionalPhoto = "https://images.unsplash.com/photo-1502086223501-7ea6ecd79368?w=600",
                westernPhoto = "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=600",
                professionalPhoto = "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?w=600",
                modelingPhoto = "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=600",
                actingPhoto = "https://images.unsplash.com/photo-1502086223501-7ea6ecd79368?w=600",
                introductionVideos = listOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
                auditionVideos = listOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4"),
                youtubeLinks = emptyList()
            ),
            measurements = ArtistMeasurements(
                heightCm = 125,
                heightFeet = "4'1\"",
                weightKg = 24,
                chestInches = 24,
                waistInches = 22,
                hipsInches = 25,
                shoeSize = "Kids UK 1",
                clothingSize = "Age 8-9Y",
                hairColor = "Black",
                eyeColor = "Black"
            ),
            verificationStatus = VerificationStatus.APPROVED,
            isVerified = true,
            postsCount = 21,
            fansCount = 9200, // "FANS"
            favesCount = 45,  // "FAVES"
            privateMobile = "+91 98333 77889 (Parent Swati Roy)",
            privateEmail = "swati.roy.guardian@gmail.com",
            privateAddress = "Tower 4, Oberoi Springs, Andheri West, Mumbai",
            emergencyContact = "+91 98333 77880 (Father Dev Roy)",
            parentGuardianConsent = true
        )
    )

    val initialStories = listOf(
        StoryItem(
            id = "STR-01",
            artistIdCode = "HT-ART-000001",
            authorName = "Priya Sharma",
            authorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            mediaUrl = "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=800",
            caption = "Day 1 on set in Film City! ✨🎥 #DreamCastCreate",
            timeAgo = "1h ago"
        ),
        StoryItem(
            id = "STR-02",
            artistIdCode = "HT-ART-000002",
            authorName = "Aryan Kapoor",
            authorAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300",
            mediaUrl = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=800",
            caption = "Pre-audition workout routine 🔥 #HetashreeStar",
            timeAgo = "3h ago"
        ),
        StoryItem(
            id = "STR-03",
            artistIdCode = "HT-ART-000003",
            authorName = "Ananya Roy",
            authorAvatarUrl = "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=300",
            mediaUrl = "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?w=800",
            caption = "Costume trial for jewellery shoot! 🌟",
            timeAgo = "5h ago"
        )
    )

    val initialPosts = listOf(
        SocialPost(
            id = "POST-01",
            artistIdCode = "HT-ART-000001",
            authorName = "Priya Sharma",
            authorUsername = "priyasharma_actor",
            authorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            isVerified = true,
            mediaUrl = "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=1000",
            isVideo = false,
            caption = "Embracing the rich royal hues of our heritage. Grateful to HETASHREE FILM'S PRODUCTION for this incredible feature project journey! What do you guys think of this royal look? ✨🪔",
            hashtags = listOf("#HetashreeStar", "#DreamCastCreate", "#RoyalCinema", "#LeadArtist", "#BollywoodGlam"),
            likesCount = 1420,
            commentsCount = 89,
            sharesCount = 34,
            isLiked = false,
            isSaved = false,
            timeAgo = "2 hours ago"
        ),
        SocialPost(
            id = "POST-02",
            artistIdCode = "HT-ART-000002",
            authorName = "Aryan Kapoor",
            authorUsername = "aryankapoor_official",
            authorAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300",
            isVerified = true,
            mediaUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=1000",
            isVideo = false,
            caption = "Intense character study for upcoming action thriller 'Shadows of Deccan'. Massive thanks to Director Kabir Mehta and HETASHREE CASTING & TALENT team! 🎬🔥",
            hashtags = listOf("#ActionCinema", "#CharacterArtist", "#CastingCall", "#BollywoodAction", "#HetashreeStar"),
            likesCount = 2890,
            commentsCount = 176,
            sharesCount = 92,
            isLiked = true,
            isSaved = true,
            timeAgo = "5 hours ago"
        ),
        SocialPost(
            id = "POST-03",
            artistIdCode = "HT-ART-000003",
            authorName = "Ananya Roy",
            authorUsername = "ananyaroy_star",
            authorAvatarUrl = "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=300",
            isVerified = true,
            mediaUrl = "https://images.unsplash.com/photo-1502086223501-7ea6ecd79368?w=1000",
            isVideo = false,
            caption = "Smiles on camera! Wrapped up the festival festive advert shoot today. Thank you everyone for the sweet love and blessings! ❤️🎉",
            hashtags = listOf("#KidsArtist", "#CommercialActor", "#LittleStar", "#HetashreeCasting"),
            likesCount = 980,
            commentsCount = 64,
            sharesCount = 18,
            isLiked = false,
            isSaved = false,
            timeAgo = "1 day ago"
        )
    )

    val initialReels = listOf(
        ReelVideo(
            id = "REEL-01",
            artistIdCode = "HT-ART-000001",
            authorName = "Priya Sharma",
            authorUsername = "priyasharma_actor",
            authorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            isVerified = true,
            videoThumbnailUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=900",
            caption = "Monologue rehearsal from Scene 42. Pure emotional intensity! Audition taped exclusively via HETASHREE STAR. 🎬🎭",
            hashtags = listOf("#Monologue", "#AuditionTape", "#DramaticActing", "#HetashreeStar"),
            audioTitle = "Original Audio - Emotional Violin & Cello Suite",
            likesCount = 4320,
            commentsCount = 245,
            sharesCount = 138,
            isLiked = true,
            isSaved = true
        ),
        ReelVideo(
            id = "REEL-02",
            artistIdCode = "HT-ART-000002",
            authorName = "Aryan Kapoor",
            authorUsername = "aryankapoor_official",
            authorAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300",
            isVerified = true,
            videoThumbnailUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=900",
            caption = "Behind the scenes choreography practice for our music video climax shoot! Sweat, beats and rhythm. ⚡️🕺",
            hashtags = listOf("#DanceReel", "#BhangraBeats", "#BollywoodDancer", "#ReelsIndia"),
            audioTitle = "Dhol Explosion Beat - Prod. HETASHREE",
            likesCount = 8920,
            commentsCount = 490,
            sharesCount = 612,
            isLiked = false,
            isSaved = false
        )
    )

    val initialComments = listOf(
        PostComment(
            id = "CMT-01",
            postId = "POST-01",
            authorName = "Vikram Malhotra",
            authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            authorRole = "Producer",
            text = "Stunning presence Priya! Looking forward to reviewing your casting submission.",
            timeAgo = "1h ago"
        ),
        PostComment(
            id = "CMT-02",
            postId = "POST-01",
            authorName = "Sunaina Oberoi",
            authorAvatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200",
            authorRole = "Casting Director",
            text = "The expressive gaze is pitch-perfect for our period drama role. Keep shining!",
            timeAgo = "45m ago"
        )
    )

    val initialCastingCalls = listOf(
        CastingCall(
            id = "CAST-2026-001",
            projectName = "Agni: The Royal Legacy",
            projectType = "Feature Films",
            productionHouse = "HETASHREE FILM'S PRODUCTION",
            roleName = "Princess Maithili (Lead Role)",
            artistCategory = "Lead Artists",
            gender = "Female",
            minAge = 22,
            maxAge = 30,
            minHeightCm = 165,
            maxHeightCm = 178,
            location = "Mumbai & Jaipur",
            shootingDates = "Dec 15, 2026 - Jan 25, 2027",
            workingDays = 35,
            languages = listOf("Hindi", "Rajasthani", "English"),
            experienceRequired = "Minimum 3 years acting or recognized theater training",
            requiredSkills = listOf("Classical Dance", "Swordplay", "Horse Riding", "Royal Demeanour"),
            projectDescription = "High-budget historical epic drama portraying courageous royal heritage and palace politics. Complete wardrobe by renowned designers.",
            budgetAmount = 750000.0,
            auditionDate = "Nov 25, 2026",
            auditionLocation = "HETASHREE Studios, Andheri West, Mumbai",
            applicationDeadline = "Nov 22, 2026",
            status = "Active",
            postedBy = "HETASHREE FILM'S PRODUCTION"
        ),
        CastingCall(
            id = "CAST-2026-002",
            projectName = "Shadows of Deccan",
            projectType = "Web Series",
            productionHouse = "Dharma Vision Productions",
            roleName = "ACP Vikram Rathore (Key Antagonist)",
            artistCategory = "Character Artists",
            gender = "Male",
            minAge = 28,
            maxAge = 40,
            minHeightCm = 175,
            maxHeightCm = 188,
            location = "Hyderabad & Pune",
            shootingDates = "Jan 10, 2027 - Feb 28, 2027",
            workingDays = 28,
            languages = listOf("Hindi", "Telugu (Basics)", "English"),
            experienceRequired = "Action background preferred, strong dialogue delivery",
            requiredSkills = listOf("Stunt fighting", "Weapon handling", "Intense expressions"),
            projectDescription = "Gritty 8-episode crime noir web series for a major pan-India OTT streaming platform.",
            budgetAmount = 450000.0,
            auditionDate = "Dec 02, 2026",
            auditionLocation = "Film Nagar Studios, Hyderabad",
            applicationDeadline = "Nov 28, 2026",
            status = "Active",
            postedBy = "Vikram Malhotra"
        ),
        CastingCall(
            id = "CAST-2026-003",
            projectName = "Sparkle Gold & Diamonds Festive TVC",
            projectType = "Jewellery Shoots",
            productionHouse = "Velvet Motion Pictures",
            roleName = "Royal Bride & Mother-Daughter Duo",
            artistCategory = "Female Models",
            gender = "Female",
            minAge = 24,
            maxAge = 32,
            minHeightCm = 168,
            maxHeightCm = 180,
            location = "Mumbai",
            shootingDates = "Dec 05, 2026 - Dec 08, 2026",
            workingDays = 4,
            languages = listOf("Hindi", "English"),
            experienceRequired = "High-fashion bridal shoots portfolio mandatory",
            requiredSkills = listOf("Traditional Grace", "Jewellery Pose", "Expressive Eyes"),
            projectDescription = "Pan-India festive jewellery television commercial and hoarding campaign for the upcoming wedding season.",
            budgetAmount = 180000.0,
            auditionDate = "Nov 20, 2026",
            auditionLocation = "Studio 9, Mehboob Studios, Bandra, Mumbai",
            applicationDeadline = "Nov 18, 2026",
            status = "Active",
            postedBy = "Kabir Mehta"
        ),
        CastingCall(
            id = "CAST-2026-004",
            projectName = "Little Champ Health Drink",
            projectType = "Commercials",
            productionHouse = "HETASHREE CASTING & TALENT",
            roleName = "Energetic Sports Kid",
            artistCategory = "Kids Artists",
            gender = "Any",
            minAge = 6,
            maxAge = 10,
            minHeightCm = 110,
            maxHeightCm = 135,
            location = "Bangalore",
            shootingDates = "Dec 12, 2026 - Dec 14, 2026",
            workingDays = 3,
            languages = listOf("Hindi", "English"),
            experienceRequired = "Spontaneous on-camera confidence",
            requiredSkills = listOf("Cycling", "Swimming/Running", "Bright Smile"),
            projectDescription = "National television advertisement showcasing energetic kids conquering daily sports challenges.",
            budgetAmount = 90000.0,
            auditionDate = "Nov 24, 2026",
            auditionLocation = "Online Self-tape Audition via HETASHREE STAR",
            applicationDeadline = "Nov 21, 2026",
            status = "Active",
            postedBy = "Sunaina Oberoi"
        )
    )

    val initialApplications = listOf(
        CastingApplication(
            id = "APP-001",
            castingCallId = "CAST-2026-001",
            projectName = "Agni: The Royal Legacy",
            roleName = "Princess Maithili (Lead Role)",
            artistIdCode = "HT-ART-000001",
            artistName = "Priya Sharma",
            artistAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            appliedDate = "2026-11-10",
            status = ApplicationStatus.SHORTLISTED,
            notes = "Exceptional monologue audition. Invited for costume look test."
        ),
        CastingApplication(
            id = "APP-002",
            castingCallId = "CAST-2026-002",
            projectName = "Shadows of Deccan",
            roleName = "ACP Vikram Rathore (Key Antagonist)",
            artistIdCode = "HT-ART-000002",
            artistName = "Aryan Kapoor",
            artistAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300",
            appliedDate = "2026-11-11",
            status = ApplicationStatus.UNDER_REVIEW,
            notes = "Action reel verified. Director Kabir Mehta reviewing."
        ),
        CastingApplication(
            id = "APP-003",
            castingCallId = "CAST-2026-004",
            projectName = "Little Champ Health Drink",
            roleName = "Energetic Sports Kid",
            artistIdCode = "HT-ART-000003",
            artistName = "Ananya Roy",
            artistAvatarUrl = "https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=300",
            appliedDate = "2026-11-09",
            status = ApplicationStatus.SELECTED,
            notes = "Selected by client! Agreement drafting initiated."
        )
    )

    val initialAgreements = listOf(
        Agreement(
            id = "HT-AGR-2026-001",
            producerName = "Vikram Malhotra",
            productionHouse = "Dharma Vision Productions",
            projectName = "Shadows of Deccan",
            artistName = "Aryan Kapoor",
            artistIdCode = "HT-ART-000002",
            projectCategory = "Web Series",
            shootingLocation = "Hyderabad & Pune Studios",
            shootingDates = "Jan 10, 2027 to Feb 28, 2027",
            workingDays = 28,
            artistPayment = 450000.0,
            agencyCommissionPercent = 25.0, // standard 25%
            processingCharge = 100.0, // standard ₹100 charge
            paymentTerms = "40% advance before shoot commencement, 30% midway, 30% post dubbing.",
            cancellationTerms = "7 days written notice required by either party. Force Majeure clauses apply.",
            confidentialityTerms = "NDA is in effect. No BTS leaks or costume reveals prior to official release.",
            agreementDate = "2026-11-10",
            status = AgreementStatus.SIGNED,
            signedByProducer = true,
            signedByArtist = true,
            verifiedByAdmin = true
        ),
        Agreement(
            id = "HT-AGR-2026-002",
            producerName = "Kabir Mehta",
            productionHouse = "Velvet Motion Pictures",
            projectName = "Sparkle Gold Festive TVC",
            artistName = "Priya Sharma",
            artistIdCode = "HT-ART-000001",
            projectCategory = "Jewellery Shoots",
            shootingLocation = "Mehboob Studios, Mumbai",
            shootingDates = "Dec 05, 2026 - Dec 08, 2026",
            workingDays = 4,
            artistPayment = 180000.0,
            agencyCommissionPercent = 25.0,
            processingCharge = 100.0,
            paymentTerms = "50% advance on signing, 50% on final pack-up day.",
            cancellationTerms = "Standard HETASHREE production guild cancellation regulations.",
            confidentialityTerms = "High value jewellery security protocols apply on set.",
            agreementDate = "2026-11-11",
            status = AgreementStatus.SENT,
            signedByProducer = true,
            signedByArtist = false,
            verifiedByAdmin = false
        )
    )

    val initialPayments = listOf(
        PaymentRecord(
            id = "HT-PAY-8801",
            agreementId = "HT-AGR-2026-001",
            projectName = "Shadows of Deccan",
            producerName = "Vikram Malhotra",
            artistName = "Aryan Kapoor",
            artistIdCode = "HT-ART-000002",
            producerPaymentAmount = 450000.0,
            agencyCommissionPercent = 25.0,
            agencyCommissionAmount = 112500.0, // 25% of 450,000
            artistPayableAmount = 337500.0,    // 75% of 450,000
            advancePaid = 135000.0,
            remainingAmount = 202500.0,
            paymentDate = "2026-11-10",
            paymentMethod = "Bank Transfer (NEFT/RTGS)",
            transactionRef = "HDFC-N99238477102",
            status = "Completed",
            receiptNotes = "Producer payment verified and artist advance disbursed successfully."
        ),
        PaymentRecord(
            id = "HT-PAY-8802",
            agreementId = "HT-AGR-2026-003",
            projectName = "Little Champ Health Drink",
            producerName = "Sunaina Oberoi (Agency Client)",
            artistName = "Ananya Roy",
            artistIdCode = "HT-ART-000003",
            producerPaymentAmount = 90000.0,
            agencyCommissionPercent = 25.0,
            agencyCommissionAmount = 22500.0, // 25% of 90,000
            artistPayableAmount = 67500.0,    // 75% of 90,000
            advancePaid = 33750.0,
            remainingAmount = 33750.0,
            paymentDate = "2026-11-11",
            paymentMethod = "Cheque Deposit (ICICI Bank)",
            transactionRef = "CHQ-004521-ICICI",
            status = "Completed",
            receiptNotes = "Processed under Guardian Trust Account Swati Roy."
        )
    )

    val initialContactRequests = listOf(
        ContactAccessRequest(
            id = "REQ-001",
            professionalUserId = "USR-PROD-01",
            professionalName = "Vikram Malhotra",
            professionalRole = "Producer",
            professionalCompany = "Dharma Vision Productions",
            artistIdCode = "HT-ART-000001",
            artistName = "Priya Sharma",
            reason = "Direct contract negotiation for lead heroine role in upcoming historical film.",
            requestDate = "2026-11-11",
            status = ContactRequestStatus.PENDING
        )
    )

    val initialAvailability = listOf(
        AvailabilityEntry("AVL-01", "HT-ART-000001", "2026-11-20", "Audition", "Look test at Mehboob Studio"),
        AvailabilityEntry("AVL-02", "HT-ART-000001", "2026-12-05", "Shoot", "Jewellery TVC Shoot Day 1"),
        AvailabilityEntry("AVL-03", "HT-ART-000001", "2026-12-06", "Shoot", "Jewellery TVC Shoot Day 2"),
        AvailabilityEntry("AVL-04", "HT-ART-000002", "2026-11-25", "Available", "Full day available for readings"),
        AvailabilityEntry("AVL-05", "HT-ART-000002", "2027-01-10", "Shoot", "Shadows of Deccan Hyderabad schedule")
    )

    val initialNotifications = listOf(
        NotificationItem(
            id = "NOTIF-01",
            title = "Casting Call Shortlisted!",
            message = "Congratulations! You have been shortlisted for 'Princess Maithili' in Agni: The Royal Legacy.",
            type = "Casting",
            timeAgo = "1h ago",
            isRead = false
        ),
        NotificationItem(
            id = "NOTIF-02",
            title = "New Contact Access Request",
            message = "Producer Vikram Malhotra requested private contact access regarding project 'Agni'.",
            type = "Contact",
            timeAgo = "3h ago",
            isRead = false
        ),
        NotificationItem(
            id = "NOTIF-03",
            title = "Payment Processed",
            message = "Artist payment of ₹1,35,000 for 'Shadows of Deccan' has been credited to your verified bank account.",
            type = "Payment",
            timeAgo = "1d ago",
            isRead = true
        ),
        NotificationItem(
            id = "NOTIF-04",
            title = "Agreement Verified",
            message = "Agreement HT-AGR-2026-001 has been approved and counter-signed by HETASHREE FILM'S PRODUCTION.",
            type = "Agreement",
            timeAgo = "2d ago",
            isRead = true
        )
    )

    val initialChatThreads = listOf(
        ChatThread(
            participantId = "USR-CAST-01",
            participantName = "Sunaina Oberoi",
            participantAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200",
            participantRole = "Casting Director",
            lastMessage = "Your monologue audition was phenomenal! Please send your close-up profile without makeup.",
            lastMessageTime = "11:20 AM",
            unreadCount = 1
        ),
        ChatThread(
            participantId = "USR-DIR-01",
            participantName = "Kabir Mehta",
            participantAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
            participantRole = "Director",
            lastMessage = "Costume measurements confirmed. See you on the 5th December at Mehboob.",
            lastMessageTime = "Yesterday",
            unreadCount = 0
        )
    )

    val initialAuditLogs = listOf(
        AuditLogEntry(
            id = "LOG-01",
            timestamp = "2026-11-12 09:15:20",
            actorName = "Super Admin",
            actorRole = "SUPER_ADMIN",
            action = "ARTIST_VERIFICATION_APPROVED",
            details = "Verified artist ID HT-ART-000001 (Priya Sharma) following document check."
        ),
        AuditLogEntry(
            id = "LOG-02",
            timestamp = "2026-11-11 16:40:12",
            actorName = "Vikram Malhotra",
            actorRole = "PRODUCER",
            action = "CONTACT_ACCESS_REQUESTED",
            details = "Requested private contact access for artist HT-ART-000001 (Reason: Feature film audition)."
        ),
        AuditLogEntry(
            id = "LOG-03",
            timestamp = "2026-11-11 14:10:05",
            actorName = "Super Admin",
            actorRole = "SUPER_ADMIN",
            action = "AGREEMENT_COUNTER_SIGNED",
            details = "Agreement HT-AGR-2026-001 approved and stamp charge of ₹100 verified."
        ),
        AuditLogEntry(
            id = "LOG-04",
            timestamp = "2026-11-10 11:00:30",
            actorName = "Super Admin",
            actorRole = "SUPER_ADMIN",
            action = "PAYMENT_PAYOUT_DISBURSED",
            details = "Commission of 25% (₹1,12,500) recorded and advance of ₹1,35,000 transferred to HT-ART-000002."
        )
    )
}
