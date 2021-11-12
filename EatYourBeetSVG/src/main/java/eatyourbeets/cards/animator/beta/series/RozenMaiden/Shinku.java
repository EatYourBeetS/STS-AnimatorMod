package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shinku extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shinku.class)
    		.SetAttack(1, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public Shinku()
    {
        super(DATA);

        Initialize(3, 3, 2, 6);
        SetUpgrade(3, 1);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Light(1, 1, 1);

        SetAffinityRequirement(Affinity.Light, 7);
        SetAffinityRequirement(Affinity.Dark, 7);
    }

    @Override
    public int GetXValue() {
        return magicNumber * player.hand.size();
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this,m, AttackEffects.SLASH_VERTICAL);

        GameActions.Bottom.Cycle(name, magicNumber).AddCallback(() -> GameActions.Bottom.ExhaustFromPile(name, 1, p.discardPile)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(GameUtilities::HasDarkAffinity)
                .SetOptions(false, true)
                .AddCallback(() ->
                {
                    CombatStats.TryActivateLimited(cardID);
                    GameActions.Bottom.RecoverHP(secondaryValue);
                }));

        GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Light, Affinity.Dark).AddConditionalCallback(() -> {
            GameActions.Bottom.GainEndurance(secondaryValue, true);
        });
    }
}


