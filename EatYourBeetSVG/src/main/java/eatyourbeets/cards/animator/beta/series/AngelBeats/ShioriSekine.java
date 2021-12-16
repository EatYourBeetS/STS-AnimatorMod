package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

import static eatyourbeets.resources.GR.Enums.CardTags.AFTERLIFE;

public class ShioriSekine extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShioriSekine.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ShioriSekine()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Light(1, 0, 0);
        SetEthereal(true);
        SetExhaust(true);
        SetAfterlife(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return CombatStats.CardsExhaustedThisTurn().size() > 0;
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Bottom.GainEndurance(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Top.PlayFromPile(name, magicNumber, m, p.exhaustPile).SetOptions(true, false).SetFilter(c -> c.hasTag(AFTERLIFE)).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                GameActions.Top.Purge(c);
            }
        });
    }
}