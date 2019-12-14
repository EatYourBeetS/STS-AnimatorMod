package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.orbs.Earth;
import eatyourbeets.resources.AnimatorResources_Strings;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class DolaStephanie extends AnimatorCard
{
    public static final String ID = Register(DolaStephanie.class.getSimpleName());

    public DolaStephanie()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0);

        SetExhaust(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetMessage(AnimatorResources_Strings.HandSelection.TEXT[1])
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            AnimatorCard card = JavaUtilities.SafeCast(cards.get(0), AnimatorCard.class);
            if (card == null)
            {
                return;
            }

            GameActions.Top.FetchFromPile(name, 1, AbstractDungeon.player.drawPile)
            .SetOptions(false, false)
            .SetFilter(card::HasSynergy);
        });
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
        }
    }
}