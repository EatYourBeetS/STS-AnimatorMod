package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.animator.beta.special.Traveler_Wish;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Traveler_Aether extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Traveler_Aether.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);
    public static final int UNIQUE_ORB_THRESHOLD = 3;
    static {
        DATA.AddPreview(new Traveler_Wish(), true);
    }


    public Traveler_Aether()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1, 1);
        SetAffinity_Light(2);
        SetAffinity_Dark(1);
        SetAffinity_Green(1);
        SetEthereal(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Aether::new, secondaryValue).AddCallback(() -> {
            int orbsInduced = 0;

            for (AbstractOrb orb : player.orbs) {
                if (GameUtilities.IsCommonOrb(orb)) {
                    GameActions.Bottom.InduceOrb(orb.makeCopy());
                    orbsInduced++;
                    if (orbsInduced >= magicNumber) {
                        break;
                    }
                }
            }

            if (GameUtilities.GetUniqueOrbsCount() >= UNIQUE_ORB_THRESHOLD && CombatStats.TryActivateSemiLimited(cardID)) {
                GameActions.Bottom.MakeCardInDrawPile(new Traveler_Wish());
            }
        });
    }
}