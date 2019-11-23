package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.RefreshHandLayoutAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.GeassPower;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Lelouch extends AnimatorCard
{
    public static final String ID = Register(Lelouch.class.getSimpleName());

    public Lelouch()
    {
        super(ID, 3, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ALL_ENEMY);

        Initialize(0, 0, 3);

        AddExtendedDescription();

        SetPurge(true);
        SetSynergy(Synergies.CodeGeass);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        RandomizedList<AbstractCard> cards = new RandomizedList<>(p.hand.group);
        cards.Remove(this);
        for (int i = 0; i < magicNumber; i++)
        {
            AbstractCard toExhaust = cards.Retrieve(AbstractDungeon.cardRandomRng);
            if (toExhaust != null)
            {
                GameActionsHelper.ExhaustCard(toExhaust, p.hand);
            }
        }

        GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
        GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.RED)));
        GameActionsHelper.AddToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));

        for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
        {
            if (!m1.hasPower(GeassPower.POWER_ID))
            {
                GameActionsHelper.ApplyPower(p, m1, new GeassPower(m1), 1);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(2);
        }
    }
}