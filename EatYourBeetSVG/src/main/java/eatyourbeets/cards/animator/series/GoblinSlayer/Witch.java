package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Witch extends AnimatorCard implements StartupCard
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
    public boolean atBattleStartPreDraw()
    {
        if (GameUtilities.InEliteRoom())
        {
            GameActions.Bottom.Add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(false)));

            return true;
        }

        return false;
    }
}