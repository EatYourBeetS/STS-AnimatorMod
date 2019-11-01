package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.Hemokinesis2Effect;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.ArrayList;

public class Shalltear extends AnimatorCard
{
    public static final String ID = Register(Shalltear.class.getSimpleName(), EYBCardBadge.Synergy);

    public Shalltear()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(6,0, 3);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ArrayList<AbstractMonster> enemies = PlayerStatistics.GetCurrentEnemies(true);
        for (AbstractMonster m1 : enemies)
        {
            //GameActionsHelper.AddToBottom(new VFXAction(new BiteEffect(m1.hb.cX, m1.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));
            GameActionsHelper.VFX(new Hemokinesis2Effect(m1.hb.cX, m1.hb.cY, p.hb.cX, p.hb.cY), 0.2f);
            GameActionsHelper.AddToBottom(new LoseHPAction(m1, p, damage));

            if (HasActiveSynergy() && PlayerStatistics.UseArtifact(m1))
            {
                GameActionsHelper.ApplyPower(p, m1, new StrengthPower(m1, -1), -1);
                GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
            }
        }

        GameActionsHelper.GainTemporaryHP(p, magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }
}