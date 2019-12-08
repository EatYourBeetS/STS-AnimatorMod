package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Excalibur extends AnimatorCard implements Hidden
{
    public static final String ID = Register(Excalibur.class.getSimpleName());

    public Excalibur()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);

        Initialize(80,0);

        SetRetain(true);
        SetExhaust(true);
        SetMultiDamage(true);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new IntangiblePlayerPower(p, 1), 1);

        GameActionsHelper.AddToBottom(new VFXAction(new BorderLongFlashEffect(Color.GOLD)));
        for (AbstractCreature m1 : GameUtilities.GetCurrentEnemies(true))
        {
            GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m1.hb_x, m1.hb_y)));
        }
        GameActionsHelper.DamageAllEnemies(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(20);
        }
    }
}