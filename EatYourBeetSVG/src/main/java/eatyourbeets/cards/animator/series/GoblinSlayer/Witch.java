package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Witch extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Witch.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL);

    public Witch()
    {
        super(DATA);

        Initialize(0, 11,2);
        SetUpgrade(0, 2, 1);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.GoblinSlayer);
        SetSpellcaster();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle && GameUtilities.InEliteRoom())
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.Add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(false)));
        }
    }
}