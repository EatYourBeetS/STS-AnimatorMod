package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Saitama extends AnimatorCard
{
    public static final String ID = CreateFullID(Saitama.class.getSimpleName());

    public Saitama()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(21, 0, 21);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        this.magicNumber = this.baseMagicNumber + PlayerStatistics.GetStrength(AbstractDungeon.player);
        this.isMagicNumberModified = magicNumber != baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.NONE);
        GameActionsHelper.AddToBottom(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

        GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, this.magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
            upgradeMagicNumber(4);
        }
    }
}