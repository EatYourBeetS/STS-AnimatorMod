package eatyourbeets.cards.animator.colorless.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.GeassPower;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Lelouch extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lelouch.class)
            .SetSkill(3, CardRarity.RARE, EYBCardTarget.ALL)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.CodeGeass);

    public Lelouch()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetCostUpgrade(-1);

        SetAffinity_Orange(1);
        SetAffinity_Blue(1);

        SetPurge(true);
        SetEthereal(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        int count = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c.uuid != uuid)
            {
                count += 1;
            }
        }

        SetUnplayable(count < magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromHand(name, magicNumber, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(info, (info2, cards) ->
        {
            if (cards.size() >= magicNumber)
            {
                final ArrayList<AbstractMonster> enemies = new ArrayList<>();
                for (AbstractMonster m2 : info2.Enemies)
                {
                    if (!m2.hasPower(GeassPower.POWER_ID))
                    {
                        enemies.add(m2);
                    }
                }

                if (enemies.size() > 0)
                {
                    GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
                    GameActions.Bottom.SFX("MONSTER_COLLECTOR_DEBUFF");
                    for (AbstractMonster m2 : enemies)
                    {
                        GameActions.Bottom.ApplyPower(player, new GeassPower(m2));
                    }
                }
            }
        });
    }
}