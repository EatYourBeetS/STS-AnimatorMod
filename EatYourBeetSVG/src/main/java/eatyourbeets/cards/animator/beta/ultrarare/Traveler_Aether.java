package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.animator.beta.special.Traveler_Wish;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class Traveler_Aether extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Traveler_Aether.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data -> data.AddPreview(new Traveler_Wish(), false));
    public static final int UNIQUE_ORB_THRESHOLD = 3;


    public Traveler_Aether()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1, 1);
        SetAffinity_Light(2);
        SetAffinity_Dark(2);
        SetAffinity_Air(1);
        SetEthereal(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Air::new, secondaryValue).AddCallback(() -> {
            int orbsInduced = 0;

            RandomizedList<AbstractOrb> orbList = new RandomizedList<AbstractOrb>(JUtils.Filter(player.orbs, GameUtilities::IsCommonOrb));
            if (orbList.Size() > 0) {
                for (int i = 0; i < magicNumber; i++) {
                    GameActions.Bottom.InduceOrb(orbList.Retrieve(rng,false).makeCopy(), true);
                }
            }

            if (CheckSpecialCondition(false) && CombatStats.TryActivateSemiLimited(cardID)) {
                GameActions.Bottom.MakeCardInDrawPile(new Traveler_Wish());
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return GameUtilities.GetUniqueOrbsCount() >= UNIQUE_ORB_THRESHOLD;
    }
}