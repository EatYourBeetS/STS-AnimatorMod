package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.ColoredSweepingBeamEffect;

public class BlackLotus extends AnimatorCard
{
    public static final String ID = CreateFullID(BlackLotus.class.getSimpleName());

    public BlackLotus()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL);

        Initialize(7, 5, 1);

        this.isMultiDamage = true;

        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_DEFECT_BEAM"));
        GameActionsHelper.AddToBottom(new VFXAction(p, new ColoredSweepingBeamEffect(p.hb.cX, p.hb.cY, p.flipHorizontal, Color.valueOf("3d0066")), 0.3F));
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.ApplyPower(p, p, new BlurPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}