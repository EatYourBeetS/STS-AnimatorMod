package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore_Lightning;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class OrbCore_LightningPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_LightningPower.class);

    public OrbCore_LightningPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount, OrbCore_Lightning.VALUE);
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE);

        final int[] damage = DamageInfo.createDamageMatrix(potency, true);
        final ArrayList<AbstractMonster> enemies = AbstractDungeon.getCurrRoom().monsters.monsters;
        for(int i = 0; i < enemies.size(); ++i)
        {
            final AbstractMonster e = enemies.get(i);
            if (!e.isDeadOrEscaped())
            {
                GameActions.Bottom.VFX(VFX.Lightning(e.hb));
                GameActions.Bottom.Add(new DamageAction(e, new DamageInfo(p, damage[i], DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
            }
        }
    }
}