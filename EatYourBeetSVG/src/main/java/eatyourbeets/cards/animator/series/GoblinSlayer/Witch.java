package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Witch extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Witch.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Spearman(), true);
    }

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Witch()
    {
        super(DATA);

        Initialize(0, 11,2);
        SetUpgrade(0, 3, 1);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Dark(1);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return Spearman.DATA.ID.equals(other.cardID) || super.HasDirectSynergy(other);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), magicNumber);

        final AbstractCard last = GameUtilities.GetLastCardPlayed(true, 1);
        if (isSynergizing && last != null && last.cardID.equals(Spearman.DATA.ID))
        {
            if (choices.TryInitialize(new Spearman()))
            {
                choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
                choices.Initialize(this);
                choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(NeutralStance.STANCE_ID));
            }

            choices.Select(1, m);
        }
    }
}