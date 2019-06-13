package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.RefreshHandLayoutAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.GeassPower;
import eatyourbeets.powers.PlayerStatistics;

public class Lelouch extends AnimatorCard
{
    public static final String ID = CreateFullID(Lelouch.class.getSimpleName());

    public Lelouch()
    {
        super(ID, 3, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ALL_ENEMY);

        Initialize(0, 0);

        this.purgeOnUse = true;

        AddExtendedDescription();

        SetSynergy(Synergies.CodeGeass);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard card : p.hand.getAttacks().group)
        {
            GameActionsHelper.ExhaustCard(card, p.hand);
        }

        GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
        GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.RED)));
        GameActionsHelper.AddToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));

        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
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